package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import example.dao.DescriptionDAO;
import example.dao.MovieDAO;
import example.model.Description;
import example.model.Movie;

public class TestDescriptionDAO {
	public static void main(String[] args) {

		Description d1 = new Description(1, "Producer A", List.of("Director 1"), List.of("Actor 1", "Actor 2"),
				LocalDate.of(2020, 5, 10));
		Description d2 = new Description(2, "Producer B", List.of("Director 2"), List.of("Actor 3", "Actor 4"),
				LocalDate.of(2021, 6, 15));
		Description d3 = new Description(3, "Producer C", List.of("Director 3"), List.of("Actor 5", "Actor 6"),
				LocalDate.of(2019, 7, 20));
		Description d4 = new Description(4, "Producer D", List.of("Director 4"), List.of("Actor 7", "Actor 8"),
				LocalDate.of(2022, 8, 25));
		Description d5 = new Description(5, "Producer E", List.of("Director 5"), List.of("Actor 9", "Actor 10"),
				LocalDate.of(2023, 9, 30));

		DescriptionDAO.getInstance().add(d1);
		DescriptionDAO.getInstance().add(d2);
		DescriptionDAO.getInstance().add(d3);
		DescriptionDAO.getInstance().add(d4);
		DescriptionDAO.getInstance().add(d5);



	}
}
