/*
 */
package devtools;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import org.netbeans.api.project.*;
import org.openide.awt.*;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;
import org.openide.util.*;
import org.openide.util.actions.Presenter;

@ActionID(category = "Tools", id = "devtools.DevToolsPopup")
@ActionRegistration(displayName = "DevTools", lazy = false)
@ActionReferences({
    @ActionReference(path = "Projects/Actions"),
    @ActionReference(path = "Projects/package/Actions"),
    @ActionReference(path = "Loaders/text/x-java/Actions"),   
    @ActionReference(path = "Loaders/Languages/Actions"),
    @ActionReference(path = "Loaders/content/unknown/Actions"),
    @ActionReference(path = "Loaders/folder/any/Actions")
})
@Messages("CTL_PopupAction=Narzędzia deweloperskie")
public final class ProjectPopupAction extends AbstractAction
        implements ActionListener,/* ContextAwareAction, */ Presenter.Popup {

    private Project project;

    @Override
    public JMenuItem getPopupPresenter() {
        Project p = Utilities.actionsGlobalContext().lookup(Project.class);
        DataFolder df = Utilities.actionsGlobalContext().lookup(DataFolder.class);

        FileObject ff = Utilities.actionsGlobalContext().lookup(FileObject.class);

        if (p != null)
            project = p;

        if (project == null)
            return null;

        JMenu main = new JMenu("DevTools");
        main.setIcon(new ImageIcon(getClass().getResource("/res/ico16.png")));
        try {
            new devtools.PopupBuilder(main, project);
        } catch (Throwable e) {
            devtools.PopupBuilder.onError(e);
        }
        return main;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("");

    }
}
