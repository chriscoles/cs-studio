package org.csstudio.utility.olog.ui;

import org.csstudio.security.ui.PasswordFieldEditor;
import org.csstudio.utility.olog.PreferenceConstants;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private StringFieldEditor urlField;
	private BooleanFieldEditor useAuthenticationField;
	private StringFieldEditor usernameField;
	private PasswordFieldEditor passwordField;

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE,
				org.csstudio.utility.olog.Activator.PLUGIN_ID));
		setMessage("Olog Client Preferences");
		setDescription("Olog preference page");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		urlField = new StringFieldEditor(PreferenceConstants.Olog_URL,
				"Olog Service URL:", getFieldEditorParent());
		// no need to override checkstate
		urlField.setEmptyStringAllowed(false);
		addField(urlField);
		useAuthenticationField = new BooleanFieldEditor(
				PreferenceConstants.Use_authentication, "use authentication",
				getFieldEditorParent());
		addField(useAuthenticationField);
		usernameField = new StringFieldEditor(PreferenceConstants.Username,
				"username:", getFieldEditorParent());
		addField(usernameField);
		passwordField = new PasswordFieldEditor(org.csstudio.utility.olog.Activator.PLUGIN_ID,
				PreferenceConstants.Password,
				"user password:", getFieldEditorParent());
		addField(passwordField);
		enableAuthenticationFields();
	}

	public void enableAuthenticationFields() {
		boolean useAuthentication = useAuthenticationField.getBooleanValue();
		usernameField.setEnabled(useAuthentication, getFieldEditorParent());
		passwordField.setEnabled(useAuthentication, getFieldEditorParent());
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		enableAuthenticationFields();
	}

	@Override
	public void setVisible(boolean visible) {
		// Override it to enable/disable user and password field depending on
		// useAuthenticationField
		super.setVisible(visible);
		enableAuthenticationFields();
	}

}
