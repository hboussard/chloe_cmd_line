package fr.inra.sad.bagap.chloe.view.wizard;

import java.util.List;
import javax.swing.JPanel;

public abstract class WizardPanel extends JPanel {

	private static final long serialVersionUID = 1L;

    /** Called when the panel is set. */
    public abstract void display();

    /** Is there be a next panel?
     * @return true if there is a panel to move to next.
     */
    public abstract boolean hasNext();

    /** Called to validate the panel before moving to next panel.
     * @param list a List of error messages to be displayed.
     * @return true if the panel is valid,
     */
    public abstract boolean validateNext(List<String> list);

    /** Get the next panel to go to. */
    public abstract WizardPanel next(Wizard wizard);

    /** Is there be a previous panel?
     * @return true if there is a panel to move to previous.
     */
    public abstract boolean hasPrevious();
    
    /** Can this panel finish the wizard?
     * @return true if this panel can finish the wizard.
     */
    public abstract boolean canRun();

    /** Called to validate the panel before finishing the wizard. Should
     * return false if canFinish returns false.
     * @param list a List of error messages to be displayed.
     * @return true if it is valid for this wizard to finish.
     */
    public abstract boolean validateRun(List<String> list);

    /** Handle finishing the wizard. */
    public abstract void run();

    /** Has this panel got help?  Defaults to false, override to change.
     * @return false if there is no help for this panel.
     */
    public boolean hasHelp() {
        return false;
    }

    /** Override this method to provide help. */
    public void help() {
    }

}
