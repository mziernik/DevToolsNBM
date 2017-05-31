package devtools;

import java.io.IOException;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

//@ServiceProvider(service = ProjectFactory.class)
public class CustomerProjectFactory implements ProjectFactory {

    public static final String PROJECT_FILE = "customer.txt";

    //Specifies when a project is a project, i.e.,
    //if "customer.txt" is present in a folder:
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_FILE) != null;
    }

    //Specifies when the project will be opened, i.e., if the project exists:
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new CustomerProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        System.out.println("");
        // leave unimplemented for the moment
    }

}

class CustomerProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;

    CustomerProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{ // register your features here
            });
        }
        return lkp;
    }

}
