package com.github.danieleieva;

import com.github.danieleieva.data.connection.PostgresConnection;
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




    @POST
    @Path("categories/create")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance categoriesPost(@NotNull @RestForm String category) {
        String error = null;
        try (var dao = connection.getDAO()) {
            dao.createCategory(category);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            error = e.getMessage();
        }
        return categories(error);
    }

    @POST @Path("categories/delete")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance categoriesRemove(@NotNull @RestForm String categoryId) {
        String error = null;
        try (var dao = connection.getDAO()) {
            dao.deleteCategory(categoryId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            error = e.getMessage();
        }
        return categories(error);
    }

    private TemplateInstance categories(@NotNull String error) {
        try (var dao = connection.getDAO()){
            return Templates.categories(dao.listCategories(), error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Templates.categories(null, error);
        }
    }
}
