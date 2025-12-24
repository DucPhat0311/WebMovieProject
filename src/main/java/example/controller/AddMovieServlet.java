package example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date; // Lưu ý import sql.Date
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import example.dao.MovieDAO;
import example.model.Movie;

@WebServlet("/admin/add-movie")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AddMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String UPLOAD_DIR = "assets" + File.separator + "img" + File.separator + "movies";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/admin/pages/movie-add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadFilePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = null;
            Part part = request.getPart("poster");
            
            if (part != null && part.getSize() > 0) {
                fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                
                // Ghi file vào ổ cứng server
                part.write(uploadFilePath + File.separator + fileName);
            } else {
                fileName = "no-image.jpg"; // Ảnh mặc định nếu không upload
            }

            // --- 2. LẤY DỮ LIỆU FORM ---
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String ageWarning = request.getParameter("ageWarning");
            String trailerUrl = request.getParameter("trailerUrl");
            
            // Xử lý số nguyên
            int duration = 0;
            try {
                duration = Integer.parseInt(request.getParameter("duration"));
            } catch (NumberFormatException e) { duration = 0; }

            // Xử lý ngày tháng 
            String dateStr = request.getParameter("releaseDate");
            Date sqlDate = new Date(System.currentTimeMillis()); // Mặc định là hôm nay
            if(dateStr != null && !dateStr.isEmpty()){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dateStr);
                sqlDate = new Date(utilDate.getTime());
            }

            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setDescription(description);
            movie.setDuration(duration);
            movie.setReleaseDate(sqlDate);
            movie.setAgeWarning(ageWarning);
            movie.setPosterUrl(fileName); 
            movie.setTrailerUrl(trailerUrl);
            movie.setActive(true);

            MovieDAO dao = new MovieDAO();
            dao.insertMovie(movie);

            response.sendRedirect("manage-movies"); 

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/pages/movie-add.jsp").forward(request, response);
        }
    }
}