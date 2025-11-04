package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import example.dao.MovieDAO;
import example.model.Description;
import example.model.Movie;

public class TestMovieDAO {
	public static void main(String[] args) {
		




			ArrayList<Movie> list = MovieDAO.getInstance().selectAll();
			for (Movie movie : list) {
				System.out.println(movie.toString());
			}
			
	}
}
