package cz.cvut.fel.mvod.prologueServer;
/*
Copyright 2011 Radovan Murin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
import cz.cvut.fel.mvod.common.Voter;
import cz.cvut.fel.mvod.global.GlobalSettingsAndNotifier;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * A class representing a window that provides user verification and final approval
 * @author Radovan Murin
 */
public class RegistrantAuthorisationWindow extends JFrame {

    JLabel status = null;
    RegistrantAuthorisationWindow instance;
    JTable table = null;
    private RowFilter<Object, Object> RowFilter;
    JTextField finder = new JTextField();
    TableRowSorter<TableModel> sorter;
/**
 * The class constructior
 */
    public RegistrantAuthorisationWindow() {
        super(GlobalSettingsAndNotifier.singleton.messages.getString("voterVerifTitle"));
        constructVerificationWindow();

        instance = this;


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
        setBounds(200, 300, 50, 50);
        setSize(300, 200);

        setVisible(true);
    }
/**
 * Construct method for the window
 */
    private void constructVerificationWindow() {
        setLayout(new BorderLayout());
        status = new JLabel(GlobalSettingsAndNotifier.singleton.messages.getString("identityCheckLabel"));
        add(status, BorderLayout.NORTH);

        XMLParser p = new XMLParser();
        finder = new JTextField();
        add(finder, BorderLayout.SOUTH);
        try {
            RegistrantTable model = new RegistrantTable(p.getRegistrants());
            table = new JTable(model);
            sorter = new TableRowSorter<TableModel>(model);
            table.setRowSorter(sorter);
            table.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        RegistrantTable r = (RegistrantTable) table.getModel();
                        Object[] options = {GlobalSettingsAndNotifier.singleton.messages.getString("confirmLabel"),
                            GlobalSettingsAndNotifier.singleton.messages.getString("cancelLabel")};
                        int n = JOptionPane.showOptionDialog(instance,
                                GlobalSettingsAndNotifier.singleton.messages.getString("confirmActionLabel"),
                                GlobalSettingsAndNotifier.singleton.messages.getString("confirmLabel"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[1]);
                        if (n == 0) {
                            RegistrantTable rt = (RegistrantTable) table.getModel();
                            Voter v = rt.getVoterAt(table.getSelectedRow());
                            FileOperator fo = new FileOperator();
                            fo.appendObjectToFile(v, "approved.voters");

                        }

                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);

            add(table, BorderLayout.CENTER);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RegistrantAuthorisationWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RegistrantAuthorisationWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RegistrantAuthorisationWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RegistrantAuthorisationWindow.class.getName()).log(Level.SEVERE, null, ex);
        }



        finder.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
            }

            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == 10) {
                    String text = finder.getText();
                    if (text.length() == 0) {
                        sorter.setRowFilter(null);

                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter(text, 3));
                    }

                }
            }

            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });



    }

    abstract class SimpleMouseListener implements MouseListener {

        public abstract void mouseClicked(MouseEvent e);

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
    /**
     * The row filter for filtering registrants
     */
    RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {

        @Override
        public boolean include(Entry entry) {
            String id = (String) entry.getValue(3);
            return id.equals(finder.getText());
        }
    };
}
