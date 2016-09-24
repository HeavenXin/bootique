package io.bootique;

import io.bootique.application.ApplicationMetadata;
import io.bootique.unit.BQInternalTestFactory;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BQCoreModule_ApplicationMetadataIT {
    @ClassRule
    public static BQInternalTestFactory runtimeFactory = new BQInternalTestFactory();

    @Test
    public void testDefault() {
        BQRuntime runtime = runtimeFactory.app().createRuntime();

        ApplicationMetadata md = runtime.getInstance(ApplicationMetadata.class);

        // we really don't know what the generated name is. It varies depending on the unit test execution environment
        assertNotNull(md.getName());
        assertNull(md.getDescription());
        assertEquals(1, md.getCommands().size());
        assertEquals(1, md.getOptions().size());
    }

    @Test
    public void testCustomDescription() {
        BQRuntime runtime = runtimeFactory.app().module(b -> BQCoreModule.setApplicationDescription(b, "app desc"))
                .createRuntime();

        ApplicationMetadata md = runtime.getInstance(ApplicationMetadata.class);

        // we really don't know what the generated name is. It varies depending on the unit test execution environment
        assertNotNull(md.getName());
        assertEquals("app desc", md.getDescription());
        assertEquals(1, md.getCommands().size());
        assertEquals(1, md.getOptions().size());
    }
}
