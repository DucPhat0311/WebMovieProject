//package example.controller;
//
//import example.dao.MovieDAO;
//
//
//import example.model.Movie;
//import java.io.*;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class MovieServlet extends HttpServlet{
////	 @Override
////	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////		    try {
////		        MovieDAO dao = MovieDAO.getInstance();
////		        List<Movie> movieList = dao.selectAll();
////
////		        request.setAttribute("movieList", movieList);
////
////		        request.getRequestDispatcher("all_movies.jsp").forward(request, response);
////		    } catch (SQLException e) {
////		        throw new ServletException(e);
////		    }
////	 }
//	 }