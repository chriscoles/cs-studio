package org.csstudio.opibuilder.visualparts;

import java.util.List;

import org.csstudio.opibuilder.script.ScriptData;
import org.csstudio.opibuilder.util.ColorService;
import org.csstudio.opibuilder.util.OPIColor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class OPIColorDialog extends Dialog {
	
	private OPIColor opiColor;
	private TableViewer preDefinedColorsViewer;
	private Label outputImageLabel, outputTextLabel;
	private String title;

	protected OPIColorDialog(Shell parentShell, OPIColor color, String dialogTitle) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.title = dialogTitle;
		if(color.isPreDefined())
			this.opiColor = new OPIColor(color.getColorName());
		else
			this.opiColor = new OPIColor(color.getRGBValue());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite parent_Composite = (Composite) super.createDialogArea(parent);
		
		final Composite mainComposite = new Composite(parent_Composite, SWT.None);			
		mainComposite.setLayout(new GridLayout(2, false));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 300;
		mainComposite.setLayoutData(gridData);
		final Composite leftComposite = new Composite(mainComposite, SWT.None);
		leftComposite.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 220;
		leftComposite.setLayoutData(gd);	
		createLabel(leftComposite, "Choose from Predefined Colors:");
		
		preDefinedColorsViewer = createPredefinedColorsTableViewer(leftComposite);
		preDefinedColorsViewer.setInput(
				ColorService.getInstance().getAllPredefinedColors());
		
		Composite rightComposite = new Composite(mainComposite, SWT.None);
		rightComposite.setLayout(new GridLayout(1, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		rightComposite.setLayoutData(gd);
		
		
		createLabel(rightComposite, "");
		
		Button colorDialogButton = new Button(rightComposite, SWT.PUSH);
		colorDialogButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		colorDialogButton.setText("Choose from Color Dialog");
		colorDialogButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dialog = new ColorDialog(Display.getCurrent().getActiveShell());
				dialog.setRGB(opiColor.getRGBValue());
				RGB rgb = dialog.open();
				if(rgb != null){
					opiColor = new OPIColor(rgb);
					preDefinedColorsViewer.setSelection(null);
					outputImageLabel.setImage(opiColor.getImage());
					outputTextLabel.setText(opiColor.getColorName());
				}
			}
		});
		
		
		Group group = new Group(rightComposite, SWT.None);
		group.setLayoutData(new GridData(SWT.FILL, SWT.END, true, true));
		group.setLayout(new GridLayout(2, false));
		group.setText("Output");
	
		outputImageLabel = new Label(group, SWT.None);
		outputImageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		outputImageLabel.setImage(opiColor.getImage());
		outputTextLabel = new Label(group, SWT.None);
		outputTextLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		outputTextLabel.setText(opiColor.getColorName());
		
		
		return parent_Composite;
	}
	
	/**
	 * Creates and configures a {@link TableViewer}.
	 * 
	 * @param parent
	 *            The parent for the table
	 * @return The {@link TableViewer}
	 */
	private TableViewer createPredefinedColorsTableViewer(final Composite parent) {
		TableViewer viewer = new TableViewer(parent, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER | SWT.SINGLE);
		viewer.setContentProvider(new BaseWorkbenchContentProvider() {
			@Override
			public Object[] getElements(final Object element) {
				return (Object[]) element;
			}
		});
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event) {
				refreshGUIOnSelection();
			}
		});
		viewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		return viewer;
	}
	
	/**
	 * Refreshes the enabled-state of the actions.
	 */
	private void refreshGUIOnSelection() {
		IStructuredSelection selection = (IStructuredSelection) preDefinedColorsViewer
				.getSelection();
		if(!selection.isEmpty() 
				&& selection.getFirstElement() instanceof OPIColor){
			opiColor = (OPIColor)selection.getFirstElement();
			outputImageLabel.setImage(opiColor.getImage());
			outputTextLabel.setText(opiColor.getColorName());
		}
	}
	
	/**
	 * Creates a label with the given text.
	 * 
	 * @param parent
	 *            The parent for the label
	 * @param text
	 *            The text for the label
	 */
	private void createLabel(final Composite parent, final String text) {
		Label label = new Label(parent, SWT.WRAP);
		label.setText(text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
				false, 1, 1));
	}

	public OPIColor getOutput() {
		return opiColor;
	}
	

}
