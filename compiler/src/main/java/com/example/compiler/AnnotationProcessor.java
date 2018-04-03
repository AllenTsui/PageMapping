package com.example.compiler;

import com.example.annotation.PageModule;
import com.example.annotation.PageModules;
import com.example.annotation.PageName;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private static final boolean DEBUG = true;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(PageName.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        debug("process apt with " + annotations.toString());
        if (annotations.isEmpty()) {
            return false;
        }
        boolean hasModule = false;
        boolean hasModules = false;
        // module
        String moduleName = "PageMapping";
        Set<? extends Element> moduleList = roundEnv.getElementsAnnotatedWith(PageModule.class);
        if (moduleList != null && moduleList.size() > 0) {
            PageModule annotation = moduleList.iterator().next().getAnnotation(PageModule.class);
            moduleName = moduleName + "_" + annotation.value();
            hasModule = true;
        }
        // modules
        String[] moduleNames = null;
        Set<? extends Element> modulesList = roundEnv.getElementsAnnotatedWith(PageModules.class);
        if (modulesList != null && modulesList.size() > 0) {
            Element modules = modulesList.iterator().next();
            moduleNames = modules.getAnnotation(PageModules.class).value();
            hasModules = true;
        }
        // RouterInit
        if (hasModules) {
            debug("generate modules PageInit");
            generateDefaultPageInit(moduleNames);
        } else if (!hasModule) {
            debug("generate default PageInit");
            generateDefaultPageInit();
        }
        return handlePage(moduleName, roundEnv);
    }

    private void generateDefaultPageInit() {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        initMethod.addStatement("PageMapping.map()");
        TypeSpec pageInit = TypeSpec.classBuilder("PageInit")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.example.pagerecord", pageInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateDefaultPageInit(String[] moduleNames) {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        for (String module : moduleNames) {
            initMethod.addStatement("PageMapping_" + module + ".map()");
        }
        TypeSpec pageInit = TypeSpec.classBuilder("PageInit")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.example.pagerecord", pageInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean handlePage(String genClassName, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(PageName.class);

        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        for (Element element : elements) {
            PageName pageName = element.getAnnotation(PageName.class);
            ClassName className;

            if (element.getKind() == ElementKind.CLASS) {
                className = ClassName.get((TypeElement) element);
                mapMethod.addStatement("com.example.pagerecord.PageRecorder.map($T.class, $S)", className, pageName.value());
            } else {
                throw new IllegalArgumentException("unknow type");
            }
        }
        TypeSpec pageMapping = TypeSpec.classBuilder(genClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(mapMethod.build())
                .build();
        try {
            JavaFile.builder("com.example.pagerecord", pageMapping)
                    .build()
                    .writeTo(filer);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    private void debug(String msg) {
        if (DEBUG) {
            messager.printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }
}
