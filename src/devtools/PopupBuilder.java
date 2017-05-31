package devtools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.module.api.support.ActionUtils;
import org.netbeans.api.project.*;
import org.openide.execution.ExecutorTask;
import org.openide.filesystems.FileObject;
import org.openide.windows.InputOutput;
import org.w3c.dom.*;

/**
 @author Miłosz Ziernik
 @date 23 października 2015
 @encoding UTF-8
 */
public class PopupBuilder {

    private final Project project;

    private final JPopupMenu popup;
    private final JMenu item;

    final static Map<JMenuItem, String> actions = new HashMap<JMenuItem, String>();

    public PopupBuilder(JPopupMenu popup, final Project project) throws Exception {
        this.project = project;
        this.popup = popup;
        this.item = null;
        popup.removeAll();
        process();
    }

    public PopupBuilder(JMenu item, final Project project) throws Exception {
        this.project = project;
        this.popup = null;
        this.item = item;
        item.removeAll();
        process();
    }

    public JMenuItem add(String s) {
        return popup != null ? popup.add(s) : item.add(s);
    }

    public Component add(Component comp) {
        return popup != null ? popup.add(comp) : item.add(comp);
    }

    private void process() throws Exception {

        actions.clear();

        if (project != null && popup != null) {
            ProjectInformation info = ProjectUtils.getInformation(project);
            add(info.getDisplayName()).setEnabled(false);
        }

        JMenuItem item = add("DevTools");
        item.setIcon(new ImageIcon(getClass().getResource("/res/ico16.png")));
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                WebKit webkit = new WebKit();
                webkit.open();
                webkit.requestActive();
            }
        });

        if (project == null)
            return;

        add(new JPopupMenu.Separator());

        buildAntTaskMenu();

        item = add("Konsola");
        item.setIcon(new ImageIcon(getClass().getResource("/res/terminal.png")));
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (System.getProperty("os.name").toLowerCase().contains("win"))
                        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start"},
                                null, new File(project.getProjectDirectory().getPath()));

                } catch (Throwable ex) {
                    onError(ex);
                }

            }
        });

        item = add("Katalog projektu");
        item.setIcon(new ImageIcon(getClass().getResource("/res/folder.png")));
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (System.getProperty("os.name").toLowerCase().contains("win"))
                        new ProcessBuilder("explorer.exe",
                                project.getProjectDirectory().getPath().replace("/", "\\")).start();
                } catch (Throwable ex) {
                    onError(ex);
                }

            }
        });

    }

    private void buildAntTaskMenu() throws Exception {

        FileObject projectDir = project.getProjectDirectory();
        final FileObject fileObject = projectDir.getFileObject("build.xml");

        if (fileObject == null)
            return;

        NodeList nodeList = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(fileObject.asBytes()))
                .getDocumentElement().getElementsByTagName("target");

        if (nodeList == null || nodeList.getLength() == 0)
            return;

        boolean hasItem = false;

        for (int i = 0; i < nodeList.getLength(); i++) {

            //We have encountered an <employee> tag.
            Node node = nodeList.item(i);
            if (!(node instanceof Element))

                continue;

            NamedNodeMap attribs = node.getAttributes();

            Node nName = attribs.getNamedItem("name");
            String name = nName != null ? nName.getNodeValue() : null;

            Node nDesc = attribs.getNamedItem("description");
            String desc = nDesc != null ? nDesc.getNodeValue() : null;

            if (name == null || desc == null || name.startsWith("-") || name.startsWith("_"))
                continue;

            hasItem = true;

            JMenuItem item = add(desc);
            item.setIcon(new ImageIcon(getClass().getResource("/res/play.png")));
            actions.put(item, name);
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        final String act = actions.get(e.getSource());
                        if (act == null)
                            throw new Error("Nie znaleziono akcji");

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    final ExecutorTask target = ActionUtils.runTarget(fileObject,
                                            new String[]{act}, null);

                                    target.getInputOutput().select(); // Otwórz okno "output"

                                    target.run();

                                } catch (Exception ex) {
                                    onError(ex);
                                }
                            }
                        }).start();

                    } catch (Exception ex) {
                        onError(ex);
                    }
                }
            });

        }

        if (hasItem)
            add(new JPopupMenu.Separator());

    }

    public static void onError(Throwable ex) {
        JOptionPane.showMessageDialog(null,
                ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage(),
                "Błąd", ex instanceof Error
                        ? JOptionPane.WARNING_MESSAGE
                        : JOptionPane.ERROR_MESSAGE);
    }

}
