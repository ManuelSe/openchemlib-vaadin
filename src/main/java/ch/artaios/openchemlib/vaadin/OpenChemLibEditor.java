package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Tag("openchemlib-editor")
@NpmPackage(value = "openchemlib", version = "8.15.0")
@JsModule("openchemlib/full.pretty.js")
@JsModule("./openchemlib-editor-init.js")
@CssImport("./openchemlib-editor.css")

public class OpenChemLibEditor extends AbstractSinglePropertyField<OpenChemLibEditor, String> implements HasSize {
    public enum Mode {
        MOLECULE,
        REACTION;
    }

    public static final String ATTRIBUTE_IDCODE = "idcode";
    public static final String ATTRIBUTE_READONLY = "readonly";
    public static final String ATTRIBUTE_MODE = "mode";
    public static final String ATTRIBUTE_FRAGMENT = "fragment";
    private ContextMenu contextMenu;

    // See comment in StructureEditor
    // For some reason PropertyDescriptor works better here (here values are only set from server)
//    private static final PropertyDescriptor<Boolean, Boolean> readonlyProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_READONLY, false);
//    private static final PropertyDescriptor<String, String> modeProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_MODE, Mode.MOLECULE.name().toLowerCase());
//    private static final PropertyDescriptor<Boolean, Boolean> fragmentProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_FRAGMENT, false);

    public OpenChemLibEditor() {
        this(true);
    }

    public OpenChemLibEditor(boolean readonly) {
        super("idcode", "", true);

        // set custom js event name
        setSynchronizedEvent("change");

        // init Vaadin specific JS
        getElement().executeJs("this.init();");


//        getElement().callJsFunction("setChangeListenerCallback(dispatchEvent(new CustomEvent('idcode-changed', {})));");
//        UI.getCurrent().getPage().executeJs("OCL.registerCustomElement();");

        setReadonly(readonly);

        contextMenu = new ContextMenu(this);
        contextMenu.addItem("Copy", event -> {
            System.out.println("Copy...");
//            getElement().callJsFunction("copy");
            getElement().executeJs("""
                    console.info('copy');
                    navigator.clipboard.writeText(this.idcode).then(r => console.info('idcode copied'));
                    """);
        });
        final MenuItem paste = contextMenu.addItem("Paste", event -> {
            System.out.println("Paste...");
            getElement().callJsFunction("paste");
//            getElement().executeJs("""
//                    // TODO handle other content types?
//                    // for debugging: list available clipboard content
//                    navigator.clipboard.read().then(clipboardItems => {
//                        console.info(clipboardItems.length + " clipboardItem");
//                        clipboardItems.forEach(item => {
//                            item.types.forEach(type => {
//                                item.getType(type).then(value => value.text().then(text => console.info("clipboardItem type: " + type + ": " + text)));
//                            })
//                        })
//                    });
//                    navigator.clipboard.readText().then(idcode => {
//                        // this.idcode = idcode;
//                        this.setAttribute('idcode', idcode);
//                        console.info('idcode pasted');
//                    });
//                    """);
        });
        paste.setEnabled(!readonly);

        final MenuItem clipboardContents = contextMenu.addItem("Get Clipboard Contents", event -> {
            System.out.println("GetClipboardContents...");
            getElement().executeJs("return navigator.clipboard.readText();").then(String.class, clipboardContent -> {
                System.out.println("ClipboardContent is: " + clipboardContent);
            });
        });
//        final MenuItem test = contextMenu.addItem("Test", event -> {
//            System.out.println("Test...");
//            getElement().executeJs("""
//                    reactionEncoder = OCL.ReactionEncoder;
//                    console.info(reactionEncoder);
//                    idcode1 = "gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@";
//                    reaction = reactionEncoder.decode(idcode1);
//                    idcode2 = reactionEncoder.encode(reaction);
//                    console.info(idcode1);
//                    console.info(idcode2);
//                    console.info(reaction);
//                    """);
//        });
        final MenuItem test = contextMenu.addItem("Test", event -> {
            System.out.println("Test...");
            getElement().executeJs("this.getMolecule().;");
        });
    }

    @Override
    public void setValue(String idcode) {
        getElement().setAttribute(ATTRIBUTE_IDCODE, idcode);
//        super.setValue(idcode);
    }

    public void setSmiles(String smiles) {
        getElement().callJsFunction("_setSmiles", smiles);
    }

    public boolean getReadonly() {
//        final String readonly = readonlyProperty.get(this);
        final String readonly = getElement().getProperty(ATTRIBUTE_READONLY);
        return readonly!=null && !readonly.equalsIgnoreCase("true");
    }
    public void setReadonly(boolean readonly) {
//        readonlyProperty.set(this, readonly);
        getElement().setAttribute(ATTRIBUTE_READONLY, readonly);
        if(contextMenu!=null)
            contextMenu.getItems().get(1).setEnabled(!readonly);
    }

    public Mode getMode() {
//        final String modeString = modeProperty.get(this);
        final String modeString = getElement().getProperty(ATTRIBUTE_MODE);
        return Mode.valueOf(modeString.toUpperCase());
    }
    public void setMode(Mode mode) {
//        modeProperty.set(this, mode.name().toLowerCase());
        getElement().setAttribute(ATTRIBUTE_MODE, mode.name().toLowerCase());
    }

    public boolean getFragment() {
//        return fragmentProperty.get(this);
        return getElement().getProperty(ATTRIBUTE_FRAGMENT)!=null;
    }
    public void setFragment(boolean fragment) {
//        fragmentProperty.set(this, fragment);
        getElement().setAttribute(ATTRIBUTE_FRAGMENT, fragment);
    }

//    public Registration addDblClickListener(ComponentEventListener<DblClickEvent> listener) {
//        return addListener(DblClickEvent.class, listener);
//    }
//
//    @DomEvent("dblclick")
//    public static class DblClickEvent extends ComponentEvent<OpenChemLibEditor> {
//        public DblClickEvent(OpenChemLibEditor source, boolean fromClient) {
//            super(source, fromClient);
//        }
//    }
}
