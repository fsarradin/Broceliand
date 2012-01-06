package net.kerflyn.broceliand.util;

import com.google.common.io.Files;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;

public class Templates {

    public static final STGroup TEMPLATE_GROUP = new STGroupDir("", "UTF-8", '$', '$');

    public static ST buildTemplate(String webpage) throws IOException {
        String raw = Files.toString(new File(webpage), UTF_8);
        ST template = new ST(raw, '$', '$');
        return TEMPLATE_GROUP.createStringTemplateInternally(template);
    }

}
