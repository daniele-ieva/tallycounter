package com.github.danieleieva;

import com.github.danieleieva.data.records.Category;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import java.util.ArrayList;
import java.util.Optional;

@CheckedTemplate
public class Templates {
    public static native TemplateInstance index(ArrayList<Category> categories);
    public static native TemplateInstance categories(ArrayList<Category> categories, Optional<String> error);

}
