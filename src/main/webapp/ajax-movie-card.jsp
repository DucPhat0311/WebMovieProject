<%-- File: WebContent/ajax-movie-card.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach items="${newList}" var="m">
    <div class="movie-card">
        <img src="${pageContext.request.contextPath}/assets/img/movies/${m.posterUrl}"
            alt="${m.title}" loading="lazy" />

        <div class="age-badge">${m.ageWarning}</div>

        <div class="overlay">
            <a href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
                class="buy-btn">Mua vé</a>
        </div>

        <div class="movie-info">
            <h3>
                <a href="${pageContext.request.contextPath}/movie-detail?id=${m.movieId}"
                    class="movie-title-link"> ${m.title} </a>
            </h3>
        </div>
    </div>
</c:forEach>