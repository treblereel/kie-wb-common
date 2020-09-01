/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.stunner.kogito.client.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import elemental2.promise.Promise;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.kogito.client.editor.MultiPageEditorContainerView;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagramImpl;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagramImpl_MapperImpl;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNViewDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.UserTask;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions_MapperImpl;
import org.kie.workbench.common.stunner.client.widgets.presenters.Viewer;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.impl.SessionEditorPresenter;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.impl.SessionViewerPresenter;
import org.kie.workbench.common.stunner.client.widgets.resources.i18n.StunnerWidgetsConstants;
import org.kie.workbench.common.stunner.core.client.annotation.DiagramEditor;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.CanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.util.CanvasFileExport;
import org.kie.workbench.common.stunner.core.client.components.layout.LayoutHelper;
import org.kie.workbench.common.stunner.core.client.components.layout.OpenDiagramLayoutExecutor;
import org.kie.workbench.common.stunner.core.client.error.DiagramClientErrorHandler;
import org.kie.workbench.common.stunner.core.client.i18n.ClientTranslationService;
import org.kie.workbench.common.stunner.core.client.service.ClientRuntimeError;
import org.kie.workbench.common.stunner.core.client.service.ServiceCallback;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;
import org.kie.workbench.common.stunner.core.client.session.impl.EditorSession;
import org.kie.workbench.common.stunner.core.client.session.impl.ViewerSession;
import org.kie.workbench.common.stunner.core.client.validation.canvas.CanvasDiagramValidator;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.DiagramImpl;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.documentation.DocumentationView;
import org.kie.workbench.common.stunner.core.rule.RuleViolation;
import org.kie.workbench.common.stunner.core.validation.DiagramElementViolation;
import org.kie.workbench.common.stunner.core.validation.DomainViolation;
import org.kie.workbench.common.stunner.forms.client.event.FormPropertiesOpened;
import org.kie.workbench.common.stunner.forms.client.widgets.FormsFlushManager;
import org.kie.workbench.common.stunner.kogito.client.docks.DiagramEditorPreviewAndExplorerDock;
import org.kie.workbench.common.stunner.kogito.client.docks.DiagramEditorPropertiesDock;
import org.kie.workbench.common.stunner.kogito.client.editor.event.OnDiagramFocusEvent;
import org.kie.workbench.common.stunner.kogito.client.menus.BPMNStandaloneEditorMenuSessionItems;
import org.kie.workbench.common.stunner.kogito.client.perspectives.AuthoringPerspective;
import org.kie.workbench.common.stunner.kogito.client.service.AbstractKogitoClientDiagramService;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilder;
import org.treblereel.gwt.jackson.api.DefaultXMLDeserializationContext;
import org.treblereel.gwt.jackson.api.DefaultXMLSerializationContext;
import org.treblereel.gwt.jackson.api.XMLDeserializationContext;
import org.treblereel.gwt.jackson.api.XMLSerializationContext;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.client.annotations.WorkbenchClientEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartTitleDecoration;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.promise.Promises;
import org.uberfire.client.workbench.events.ChangeTitleWidgetEvent;
import org.uberfire.client.workbench.widgets.common.ErrorPopupPresenter;
import org.uberfire.ext.widgets.core.client.editors.texteditor.TextEditorView;
import org.uberfire.lifecycle.GetContent;
import org.uberfire.lifecycle.GetPreview;
import org.uberfire.lifecycle.IsDirty;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnFocus;
import org.uberfire.lifecycle.OnLostFocus;
import org.uberfire.lifecycle.OnMayClose;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.lifecycle.SetContent;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.Menus;

@ApplicationScoped
@DiagramEditor
@WorkbenchClientEditor(identifier = BPMNDiagramEditor.EDITOR_ID)
public class BPMNDiagramEditor extends AbstractDiagramEditor {

    public static final String EDITOR_ID = "BPMNDiagramEditor";

    private final DiagramEditorPreviewAndExplorerDock diagramPreviewAndExplorerDock;
    private final DiagramEditorPropertiesDock diagramPropertiesDock;
    private final LayoutHelper layoutHelper;
    private final OpenDiagramLayoutExecutor openDiagramLayoutExecutor;
    protected final AbstractKogitoClientDiagramService diagramServices;
    protected final FormsFlushManager formsFlushManager;
    private final CanvasFileExport canvasFileExport;
    private final Promises promises;
    private final CanvasDiagramValidator<AbstractCanvasHandler> validator;

    protected String formElementUUID;

    @Inject
    public BPMNDiagramEditor(final View view,
                             final FileMenuBuilder fileMenuBuilder,
                             final PlaceManager placeManager,
                             final MultiPageEditorContainerView multiPageEditorContainerView,
                             final Event<ChangeTitleWidgetEvent> changeTitleNotificationEvent,
                             final Event<NotificationEvent> notificationEvent,
                             final Event<OnDiagramFocusEvent> onDiagramFocusEvent,
                             final TextEditorView xmlEditorView,
                             final ManagedInstance<SessionEditorPresenter<EditorSession>> editorSessionPresenterInstances,
                             final ManagedInstance<SessionViewerPresenter<ViewerSession>> viewerSessionPresenterInstances,
                             final BPMNStandaloneEditorMenuSessionItems menuSessionItems,
                             final ErrorPopupPresenter errorPopupPresenter,
                             final DiagramClientErrorHandler diagramClientErrorHandler,
                             final ClientTranslationService translationService,
                             final DocumentationView documentationView,
                             final DiagramEditorPreviewAndExplorerDock diagramPreviewAndExplorerDock,
                             final DiagramEditorPropertiesDock diagramPropertiesDock,
                             final LayoutHelper layoutHelper,
                             final OpenDiagramLayoutExecutor openDiagramLayoutExecutor,
                             final AbstractKogitoClientDiagramService diagramServices,
                             final FormsFlushManager formsFlushManager,
                             final CanvasFileExport canvasFileExport,
                             final Promises promises,
                             final CanvasDiagramValidator<AbstractCanvasHandler> validator) {
        super(view,
              fileMenuBuilder,
              placeManager,
              multiPageEditorContainerView,
              changeTitleNotificationEvent,
              notificationEvent,
              onDiagramFocusEvent,
              xmlEditorView,
              editorSessionPresenterInstances,
              viewerSessionPresenterInstances,
              menuSessionItems,
              errorPopupPresenter,
              diagramClientErrorHandler,
              translationService,
              documentationView);
        this.diagramPreviewAndExplorerDock = diagramPreviewAndExplorerDock;
        this.diagramPropertiesDock = diagramPropertiesDock;
        this.layoutHelper = layoutHelper;
        this.openDiagramLayoutExecutor = openDiagramLayoutExecutor;
        this.diagramServices = diagramServices;
        this.canvasFileExport = canvasFileExport;
        this.formsFlushManager = formsFlushManager;
        this.promises = promises;
        this.validator = validator;
    }

    @OnStartup
    @SuppressWarnings("unused")
    public void onStartup(final PlaceRequest place) {
        superDoStartUp(place);
        initDocks();
        getWidget().init(this);
    }

    void superDoStartUp(final PlaceRequest place) {
        super.doStartUp(place);
    }

    @Override
    public void open(final Diagram diagram,
                     final Viewer.Callback callback) {
        this.layoutHelper.applyLayout(diagram, openDiagramLayoutExecutor);
        super.open(diagram, callback);
    }

    @OnOpen
    @SuppressWarnings("unused")
    public void onOpen() {
        super.doOpen();
    }

    @OnClose
    @SuppressWarnings("unused")
    public void onClose() {
        superOnClose();
        closeDocks();
    }

    void superOnClose() {
        super.doClose();
    }

    @Override
    public void onDiagramLoad() {
        final Optional<CanvasHandler> canvasHandler = Optional.ofNullable(getCanvasHandler());

        canvasHandler.ifPresent(c -> {
            final Metadata metadata = c.getDiagram().getMetadata();
            metadata.setPath(makeMetadataPath(metadata.getRoot(), metadata.getTitle()));
            openDocks();
        });
    }

    private Path makeMetadataPath(final Path root,
                                  final String title) {
        final String uri = root.toURI();
        return PathFactory.newPath(title, uri + "/" + title + ".bpmn");
    }

    @OnFocus
    @SuppressWarnings("unused")
    public void onFocus() {
        superDoFocus();
        onDiagramLoad();
    }

    void superDoFocus() {
        super.doFocus();
    }

    @OnLostFocus
    @SuppressWarnings("unused")
    public void onLostFocus() {
        super.doLostFocus();
    }

    @Override
    @WorkbenchPartTitleDecoration
    public IsWidget getTitle() {
        return super.getTitle();
    }

    @WorkbenchPartTitle
    public String getTitleText() {
        return "";
    }

    @WorkbenchMenu
    public void getMenus(final Consumer<Menus> menusConsumer) {
        menusConsumer.accept(super.getMenus());
    }

    @Override
    protected void makeMenuBar() {
        if (!menuBarInitialized) {
            getMenuSessionItems().populateMenu(getFileMenuBuilder());
            makeAdditionalStunnerMenus(getFileMenuBuilder());
            menuBarInitialized = true;
        }
    }

    @Override
    @WorkbenchPartView
    public IsWidget asWidget() {
        return super.asWidget();
    }

    @OnMayClose
    public boolean onMayClose() {
        return super.mayClose();
    }

    @Override
    public String getEditorIdentifier() {
        return EDITOR_ID;
    }

    @GetContent
    @Override
    public Promise getContent() {
        flush();
        validateDiagram(getCanvasHandler());

        GWT.log("getContent 1 " + getEditor().getEditorProxy().getContentSupplier().get().projectDiagram().get().getClass().getCanonicalName());
        GWT.log("getContent 2 " + getEditor().getEditorProxy().getContentSupplier().get().projectDiagram().get().getGraph().getContent().getClass().getCanonicalName());


        //KogitoDiagramResourceImpl impl = getEditor().getEditorProxy().getContentSupplier().get();



        DiagramImpl diagram = convert(getEditor().getEditorProxy().getContentSupplier().get().projectDiagram().get());

        Definitions definitions = new Definitions();
        List<BPMNViewDefinition> definitionList = new LinkedList<>();

        diagram.getGraph().nodes().forEach(n -> {
            org.kie.workbench.common.stunner.core.graph.impl.NodeImpl elm = (org.kie.workbench.common.stunner.core.graph.impl.NodeImpl) n;
            GWT.log("n 1 " + elm + " " + elm.asNode().getClass().getCanonicalName());
            GWT.log("n 2 " + elm + " " + elm.asNode().getContent().getClass().getCanonicalName());
            BPMNDiagramImpl bpmnDiagram;
            if(elm.asNode().getContent() instanceof  org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl) {

                org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl impl = (org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl) elm.asNode().getContent();

                GWT.log("impl " + impl.getDefinition() + " " + impl.getDefinition().getClass().getCanonicalName());


                if(impl.getDefinition() instanceof UserTask) {
                    UserTask userTask = (UserTask) impl.getDefinition();
                    GWT.log("            UserTask " + userTask);



                    userTask.getExtensionElements();

                    //GWT.log("JSON \n " + JSON.stringify(userTask));

                    definitionList.add(userTask);

                } else if(impl.getDefinition() instanceof BPMNDiagramImpl) {
                    bpmnDiagram = (BPMNDiagramImpl) impl.getDefinition();
                    definitions.setProcess(bpmnDiagram.getDiagramSet());
                    definitions.getProcess().setDefinitionList(definitionList);
                    definitions.setBpmnDiagram(bpmnDiagram);

                    bpmnDiagram.getDimensionsSet();
                    bpmnDiagram.getAdvancedData();
                    bpmnDiagram.getBackgroundSet();

                    bpmnDiagram.getAdvancedData().getMetaDataAttributes().getValue();

                    try {
                        GWT.log("XML 2 \n"+ BPMNDiagramImpl_MapperImpl.INSTANCE.write(bpmnDiagram));
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }

                }

            }

        });


        //TODO
        //BPMNDiagramImpl bpmnDiagram = new BPMNDiagramImpl();
        //bpmnDiagram

        XMLSerializationContext serializationContext =
                DefaultXMLSerializationContext.builder().wrapCollections(false).build();

        XMLDeserializationContext deserializationContext =
                DefaultXMLDeserializationContext.builder().wrapCollections(false).build();

        try {
            GWT.log("XML 1 \n"+Definitions_MapperImpl.INSTANCE.write(definitions, serializationContext));
            //GWT.log("XML 2 \n"+ BPMNDiagramImpl_MapperImpl.INSTANCE.write(bpmnDiagram));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        GWT.log("? "+diagram.getGraph().getContent());

/*        org.kie.workbench.common.stunner.bpmn.BPMNDefinitionSet set = (org.kie.workbench.common.stunner.bpmn.BPMNDefinitionSet) diagram.getGraph().getContent();

        GWT.log("SET " + set + " " + set.getClass().getCanonicalName());*/


        //diagram.get

        //getEditor().getEditorProxy().getContentSupplier().get()

        //GWT.log("getContent 3 " + getEditor().getEditorProxy().getContentSupplier().get().xmlDiagram().get());

        return diagramServices.transform(getEditor().getEditorProxy().getContentSupplier().get());
    }

    private DiagramImpl convert(final Diagram diagram) {
        return new DiagramImpl(diagram.getName(),
                diagram.getGraph(),
                diagram.getMetadata());
    }

    private void validateDiagram(CanvasHandler canvasHandler) {
        getSessionPresenter().displayNotifications(t -> false);

        validator.validate((AbstractCanvasHandler) canvasHandler, violations -> {
            String errorMessage = getTranslationService().getValue(StunnerWidgetsConstants.MarshallingResponsePopup_ErrorMessageLabel);

            if (!violations.isEmpty()) {
                List<String> violationMessages = new ArrayList<>();
                for (DiagramElementViolation<RuleViolation> next : violations) {
                    final Collection<DomainViolation> domainViolations = next.getDomainViolations();
                    domainViolations.forEach(item -> violationMessages.add(errorMessage + ": " + item.getUUID() + " - " + item.getMessage() + " - " + item.getViolationType()));
                }
                getEditor().getSessionPresenter().getView().showWarning(errorMessage + "(s): " + violationMessages);
            }
        });
        getSessionPresenter().displayNotifications(t -> true);
    }

    @GetPreview
    public Promise getPreview() {
        CanvasHandler canvasHandler = getCanvasHandler();
        if (canvasHandler != null) {
            return Promise.resolve(canvasFileExport.exportToSvg((AbstractCanvasHandler) canvasHandler));
        } else {
            return Promise.resolve("");
        }
    }

    @Override
    @IsDirty
    public boolean isDirty() {
        return super.isDirty();
    }

    @SetContent
    @Override
    @SuppressWarnings("all")
    public Promise setContent(final String path, final String value) {
        Promise<Void> promise =
                promises.create((success, failure) -> {
                    superOnClose();
                    diagramServices.transform(path,
                                              value,
                                              new ServiceCallback<Diagram>() {

                                                  @Override
                                                  public void onSuccess(final Diagram diagram) {
                                                      getEditor().open(diagram,
                                                                       new Viewer.Callback() {
                                                                           @Override
                                                                           public void onSuccess() {
                                                                               success.onInvoke((Void) null);
                                                                           }

                                                                           @Override
                                                                           public void onError(ClientRuntimeError error) {
                                                                               BPMNDiagramEditor.this.getEditor().onLoadError(error);
                                                                               failure.onInvoke(error);
                                                                           }
                                                                       });
                                                  }

                                                  @Override
                                                  public void onError(final ClientRuntimeError error) {
                                                      BPMNDiagramEditor.this.getEditor().onLoadError(error);
                                                      failure.onInvoke(error);
                                                  }
                                              });
                });
        return promise;
    }

    @Override
    public void resetContentHash() {
        setOriginalContentHash(getCurrentDiagramHash());
    }

    void initDocks() {
        diagramPropertiesDock.init(AuthoringPerspective.PERSPECTIVE_ID);
        diagramPreviewAndExplorerDock.init(AuthoringPerspective.PERSPECTIVE_ID);
    }

    void openDocks() {
        diagramPropertiesDock.open();
        diagramPreviewAndExplorerDock.open();
    }

    void closeDocks() {
        diagramPropertiesDock.close();
        diagramPreviewAndExplorerDock.close();
    }

    void onFormsOpenedEvent(@Observes FormPropertiesOpened event) {
        formElementUUID = event.getUuid();
    }

    void flush() {
        if (getSessionPresenter() != null) {
            ClientSession session = getSessionPresenter().getInstance();
            formsFlushManager.flush(session, formElementUUID);
        }
    }
}
