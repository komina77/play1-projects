package controllers;

import java.io.InputStream;

import org.apache.commons.io.input.ClosedInputStream;

import jobs.AttachmentsCleaner;
import models.ImageFile;
import play.Logger;
import play.db.jpa.Blob;
import play.mvc.Controller;
import play.mvc.With;

@With(CommonController.class)
public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void test() {
        InputStream is = new ClosedInputStream();
        Blob blob = new Blob();
        blob.set(is, "image/jpeg");
        Logger.info("new blob:%s", blob.getUUID());
    }

}