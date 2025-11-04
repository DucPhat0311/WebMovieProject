package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import example.dao.MovieDAO;
import example.model.Description;
import example.model.Movie;

public class TestMovieDAO {
	public static void main(String[] args) {
		Movie m1 = new Movie(1, "Inception", "Sci-Fi", 148, "USA", "A dream within a dream.", 1,
				"https://upload.wikimedia.org/wikipedia/en/a/a7/IF_%28film%29_poster_2.jpg", 8, "13+",
				"https://video.com/inception");

		Movie m2 = new Movie(2, "Avatar", "Adventure", 162, "USA", "Pandora world & Navi people.", 2,
				"https://upload.wikimedia.org/wikipedia/en/a/a7/IF_%28film%29_poster_2.jpg", 8, "13+",
				"https://video.com/avatar");

		Movie m3 = new Movie(3, "Interstellar", "Sci-Fi", 169, "USA", "Space, black hole, time warp.", 3,
				"https://upload.wikimedia.org/wikipedia/en/a/a7/IF_%28film%29_poster_2.jpg", 8, "13+",
				"https://video.com/interstellar");

		Movie m4 = new Movie(4, "Titanic", "Drama", 195, "USA", "Ship sinking love story.", 4,
				"https://upload.wikimedia.org/wikipedia/en/a/a7/IF_%28film%29_poster_2.jpg", 7, "13+",
				"https://video.com/titanic");

		Movie m5 = new Movie(5, "Avengers: Endgame", "Action", 181, "USA", "Heroes vs Thanos", 5,
				"https://upload.wikimedia.org/wikipedia/en/a/a7/IF_%28film%29_poster_2.jpg", 8, "13+",
				"https://video.com/endgame");


//		MovieDAO.getInstance().add(m1);
		MovieDAO.getInstance().add(m2);
//		MovieDAO.getInstance().add(m3);
//		MovieDAO.getInstance().add(m4);
//		MovieDAO.getInstance().add(m5);

			ArrayList<Movie> list = MovieDAO.getInstance().selectAll();
			for (Movie movie : list) {
				System.out.println(movie.toString());
			}
			
	}
}
