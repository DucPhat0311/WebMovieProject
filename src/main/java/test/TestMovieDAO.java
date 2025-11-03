package test;

import java.util.ArrayList;

import example.dao.MovieDAO;
import example.model.Movie;

public class TestMovieDAO {
	public static void main(String[] args) {
		ArrayList<Movie> list = MovieDAO.getInstance().selectAll();
		for (Movie movie : list) {
			System.out.println(movie.toString());
		}
		
	}
}
