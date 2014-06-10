package edu.morgan.pafafu.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.morgan.pafafu.domain.Question;
import edu.morgan.pafafu.service.QuestionService;

@SuppressWarnings("serial")
public class RecentActivityServlet extends HttpServlet {

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    QuestionService questionService = new QuestionService();

    List<Question> answeredQuestions = questionService.getAnswered();
    request.setAttribute("answered", answeredQuestions);

    List<Question> unansweredQuestions = questionService.getUnanswered();
    request.setAttribute("unanswered", unansweredQuestions);

    getServletContext().getRequestDispatcher("/WEB-INF/jsp/template.jsp").
        forward(request, response);
  }
}
