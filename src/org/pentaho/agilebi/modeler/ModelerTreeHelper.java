package org.pentaho.agilebi.modeler;

import org.pentaho.agilebi.modeler.nodes.AbstractMetaDataModelNode;
import org.pentaho.agilebi.modeler.nodes.AvailableField;
import org.pentaho.agilebi.modeler.propforms.ModelerNodePropertiesForm;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulEventSourceAdapter;
import org.pentaho.ui.xul.components.XulConfirmBox;
import org.pentaho.ui.xul.containers.XulDeck;
import org.pentaho.ui.xul.dom.Document;
import org.pentaho.ui.xul.stereotype.Bindable;
import org.pentaho.ui.xul.util.AbstractModelNode;
import org.pentaho.ui.xul.util.XulDialogCallback;

import java.util.Map;

/**
 * Created: 3/21/11
 *
 * @author rfellows
 */
public abstract class ModelerTreeHelper extends XulEventSourceAdapter {

  private transient Object selectedTreeItem;
  private Map<Class<? extends ModelerNodePropertiesForm>, ModelerNodePropertiesForm> propertiesForms;
  private ModelerNodePropertiesForm selectedForm;
  private XulDeck propsDeck;
  protected ModelerWorkspace workspace;
  private AvailableField[] availableFields;
  private Document document;

  public ModelerTreeHelper() {
  }

  public ModelerTreeHelper(Map<Class<? extends ModelerNodePropertiesForm>, ModelerNodePropertiesForm> propertiesForms,
                           XulDeck propsDeck, ModelerWorkspace workspace, AvailableField[] availableFields,
                           Document document) {
    this.propertiesForms = propertiesForms;
    this.propsDeck = propsDeck;
    this.workspace = workspace;
    this.availableFields = availableFields;
    this.document = document;
  }

  @Bindable
  public Object getSelectedTreeItem() {
    return selectedTreeItem;
  }

  @Bindable
  public void setSelectedTreeItem(Object selectedTreeItem) {
    this.selectedTreeItem = selectedTreeItem;
  }

  @Bindable
  public void setTreeSelectionChanged(Object selection) {
    setSelectedTreeItem(selection);
    if (selection != null && selection instanceof AbstractMetaDataModelNode) {
      AbstractMetaDataModelNode node = (AbstractMetaDataModelNode) selection;
      ModelerNodePropertiesForm form = propertiesForms.get(node.getPropertiesForm());
      if (form != null) {
        if (selectedForm != null && selectedForm != form) {
          selectedForm.setObject(null);
        }
        form.activate((AbstractMetaDataModelNode) selection);
        selectedForm = form;
        return;
      }
    }
    if (propsDeck != null) {
      propsDeck.setSelectedIndex(0);
    }
  }

  @Bindable
  public void moveFieldUp() {
    if (selectedTreeItem == null) {
      return;
    }
    ((AbstractModelNode) selectedTreeItem).getParent().moveChildUp(selectedTreeItem);

  }

  @Bindable
  public void moveFieldDown() {
    if (selectedTreeItem == null) {
      return;
    }
    ((AbstractModelNode) selectedTreeItem).getParent().moveChildDown(selectedTreeItem);

  }

  @Bindable
  public void removeField() {
    ((AbstractModelNode) selectedTreeItem).getParent().remove(selectedTreeItem);
    setTreeSelectionChanged(null);
  }

  @Bindable
  public void clearFields(){
	  try {

		  XulConfirmBox confirm = (XulConfirmBox) document.createElement("confirmbox"); //$NON-NLS-1$
          confirm.setTitle(ModelerMessagesHolder.getMessages().getString("clear_model_title")); //$NON-NLS-1$
          confirm.setMessage(ModelerMessagesHolder.getMessages().getString("clear_model_msg")); //$NON-NLS-1$
          confirm.setAcceptLabel(ModelerMessagesHolder.getMessages().getString("yes")); //$NON-NLS-1$
          confirm.setCancelLabel(ModelerMessagesHolder.getMessages().getString("no")); //$NON-NLS-1$

          confirm.addDialogCallback(new XulDialogCallback() {
        	  public void onClose( XulComponent sender, Status returnCode, Object retVal ) {
        		  if (returnCode == Status.ACCEPT) {
                clearTreeModel();
        		  }
        	  }

        	  public void onError( XulComponent sender, Throwable t ) {
        	  }
         });
         confirm.open();
      } catch (Exception e) {
        e.printStackTrace();//logger.error(e);
      }
  }

  @Bindable
  public abstract void clearTreeModel();

  @Bindable
  public abstract void addField();

  @Bindable
  public Object[] getSelectedFields() {
    if (availableFields == null) {
      availableFields = new AvailableField[]{};
    }
    return availableFields;
  }

  public Map<Class<? extends ModelerNodePropertiesForm>, ModelerNodePropertiesForm> getPropertiesForms() {
    return propertiesForms;
  }

  public void setPropertiesForms(Map<Class<? extends ModelerNodePropertiesForm>, ModelerNodePropertiesForm> propertiesForms) {
    this.propertiesForms = propertiesForms;
  }

  public ModelerNodePropertiesForm getSelectedForm() {
    return selectedForm;
  }

  public void setSelectedForm(ModelerNodePropertiesForm selectedForm) {
    this.selectedForm = selectedForm;
  }

  public XulDeck getPropsDeck() {
    return propsDeck;
  }

  public void setPropsDeck(XulDeck propsDeck) {
    this.propsDeck = propsDeck;
  }

  public ModelerWorkspace getWorkspace() {
    return workspace;
  }

  public void setWorkspace(ModelerWorkspace workspace) {
    this.workspace = workspace;
  }

  public AvailableField[] getAvailableFields() {
    return availableFields;
  }

  public void setAvailableFields(AvailableField[] availableFields) {
    this.availableFields = availableFields;
  }

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }
}