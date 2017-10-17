package fr.inra.sad_paysage.chloe.view.wizard;

public interface WizardListener {

	 /** Called when the wizard run.
     * @param wizard the wizard that run.
     */
    public void wizardRun(Wizard wizard);

    /** Called when the wizard is cancelled.
     * @param wizard the wizard that was cancelled.
     */
    public void wizardCancelled(Wizard wizard);

    /** Called when a new panel has been displayed in the wizard.
     * @param wizard the wizard that was updated
     */
    public void wizardPanelChanged(Wizard wizard);
	
}
