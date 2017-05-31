/*
 */
package devtools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.netbeans.api.project.*;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.*;

@ActionID(
        category = "Tools",
        id = "devtools.ToolbarButton"
)
@ActionRegistration(iconBase = "res/ico24.png", displayName = "#CTL_ToolbarButton")
@ActionReferences({
    @ActionReference(path = "Toolbars/Build", position = -20),})
@Messages("CTL_ToolbarButton=DevTools")
public final class ToolbarButton implements ActionListener {

    final static JPopupMenu popup = new JPopupMenu();

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Project project = OpenProjects.getDefault().getMainProject();
            if (project == null)
                project = Utilities.actionsGlobalContext().lookup(Project.class);
            new PopupBuilder(popup, project);
            JButton btn = (JButton) e.getSource();
            popup.show(btn, 0, btn.getHeight());
        } catch (Throwable ex) {
            PopupBuilder.onError(ex);
        }
    }
}
