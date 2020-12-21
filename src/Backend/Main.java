package Backend;

import express.Express;
import express.middleware.Middleware;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Express app = new Express();
        Database db = new Database();


        //ladda upp en bild
        app.post("/api/upload/image", (req, res) -> {
            String imageUrl = null;

            try {
                List<FileItem> images = req.getFormData("images");
                imageUrl = db.uploadImage(images.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            res.send(imageUrl);
        });


        //ladda upp en fil
        app.post("/api/upload/file", (req, res) -> {
            String fileUrl = null;

            try {
                List<FileItem> files = req.getFormData("files");
                fileUrl = db.uploadFile(files.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            res.send(fileUrl);
        });



        app.get("/rest/url-ids", (req, res) -> {
            List<Url> urlids = db.getURLids();
            res.json(urlids);
        });

        app.get("/rest/notes/:id", (req, res) -> {
            int url_id = Integer.parseInt(req.getParam("id"));

            List<Note> notes = db.getNotes(url_id);
            res.json(notes);

        });

        app.get("/rest/notes", (req, res) -> {
            List<Note> notes = db.adminGetNotes();
            res.json(notes);
        });


        //radera en note
        app.delete("/rest/notes/delete", (req, res) -> {
            Note note = (Note) req.getBody(Note.class);

            db.deleteNote(note);

            res.json(note);
        });


        //skapa en note
        app.post("/rest/notes", (req, res) -> {

            Note note = (Note) req.getBody(Note.class);

            db.createNote(note);

            res.send("post OK");
        });


        //uppdatera en note
        app.post("/rest/notes/update", (req, res) -> {
            Note note = (Note) req.getBody(Note.class);

            db.updateNote(note);

            res.send("update OK");
        });

        app.get("/rest/images", (req, res) -> {
            List<Image> images = db.adminGetImages();
            res.json(images);
        });

        app.get("/rest/files", (req, res) -> {
            List<File> files = db.adminGetFiles();
            res.json(files);
        });

        app.get("/rest/images/:id", (req, res) -> {
            int url_id = Integer.parseInt(req.getParam("id"));

            List<Image> images = db.getImages(url_id);

            res.json(images);

        });

        app.get("/rest/files/:id", (req, res) -> {
            int url_id = Integer.parseInt(req.getParam("id"));

            List<File> files = db.getFiles(url_id);

            res.json(files);

        });

        try {
            app.use(Middleware.statics(Paths.get("src/Frontend").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        app.listen(3000); // defaults to port 80
        System.out.println("Server started on port 3000");


    }
}
