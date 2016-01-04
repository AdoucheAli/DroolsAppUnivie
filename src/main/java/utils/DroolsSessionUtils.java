package utils;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.io.ResourceFactory;

public class DroolsSessionUtils {

    private DroolsSessionUtils() {}

    public static KieSession createKieSession(String... drlResourcesPaths) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kcontainer = createKieContainer(ks, drlResourcesPaths);

        // Configure and create the KieBase
        KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
        KieBase kbase = kcontainer.newKieBase(kbconf);

        // Configure and create the KieSession
        KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
        return kbase.newKieSession(ksconf, null);
    }

    private static KieContainer createKieContainer(KieServices ks, String... drlResourcesPaths) {
        // Create the in-memory File System and add the resources files to it
        KieFileSystem kfs = ks.newKieFileSystem();
        for (String path : drlResourcesPaths) {
            kfs.write(ResourceFactory.newClassPathResource(path));
        }
        // Create the builder for the resources of the File System
        KieBuilder kbuilder = ks.newKieBuilder(kfs);
        // Build the Kie Bases
        kbuilder.buildAll();
        // Check for errors
        if (kbuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new IllegalArgumentException(kbuilder.getResults().toString());
        }
        // Get the Release ID (mvn style: groupId, artifactId,version)
        ReleaseId relId = kbuilder.getKieModule().getReleaseId();
        // Create the Container, wrapping the KieModule with the given ReleaseId
        return ks.newKieContainer(relId);
    }
}
