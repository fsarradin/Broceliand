package net.kerflyn.broceliand.util;

import com.google.common.io.Files;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;

public class Templates {

    public static ST buildTemplate(String webpage) throws IOException {
        String raw = Files.toString(new File(webpage), UTF_8);
        return new ST(raw, '$', '$');
    }

}
