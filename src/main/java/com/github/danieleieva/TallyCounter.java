package com.github.danieleieva;

import com.github.danieleieva.data.connection.PostgresConnection;
import com.github.danieleieva.data.records.Category;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.constraint.NotNull;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;

import java.util.ArrayList;
import java.util.Optional;


@Path("/")
public class TallyCounter {

    @Inject
    PostgresConnection connection;

    private static final Logger log = LoggerFactory.getLogger(TallyCounter.class);

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance indexGet() {
        try (var db = connection.getDAO()){
            return Templates.index(db.listCategories());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return Templates.index(new ArrayList<>());
    }

    @GET
    @Path("categories")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance categoriesGet() {
        try (var dao = connection.getDAO()){
            return Templates.categories(dao.listCategories(), Optional.empty());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Templates.categories(new ArrayList<>(), Optional.of(e.getMessage()));
        }
    }

    @POST
    @Path("categories")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance categoriesPost(@NotNull @RestForm String category) {
        String error = null;
        ArrayList<Category> categories = null;
        try (var dao = connection.getDAO()) {
            dao.createCategory(category);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            error = e.getMessage();
        }
        try (var dao = connection.getDAO()){
            return Templates.categories(dao.listCategories(), Optional.ofNullable(error));
        } catch (Exception e) { 
            log.error(e.getMessage(), e);
            return Templates.categories(new ArrayList<>(), Optional.of(e.getMessage()));
        }
    }
}
