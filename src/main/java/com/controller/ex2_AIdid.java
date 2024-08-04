package com.controller;

public class ex2_AIdid {

    클래스 선언 및 서블릿 주석:
            •	@WebServlet("/board"): 이 서블릿은 /board 경로로 들어오는 요청을 처리합니다.
	•	init() 메서드:
            •	서블릿 초기화 시 BoardDAO 객체를 생성하여 데이터베이스 연결 세부 정보를 설정합니다.
	•	doGet() 및 doPost() 메서드:
            •	요청 메서드에 따라 적절한 작업을 수행합니다.
            •	action 파라미터에 따라 게시글 목록, 보기, 삭제 등의 작업을 처리합니다.
            •	listBoards() 메서드:
            •	모든 게시글을 조회하여 boardList.jsp로 포워딩합니다.
            •	viewBoard() 메서드:
            •	특정 게시글을 조회하여 boardView.jsp로 포워딩합니다.
            •	addBoard() 메서드:
            •	새 게시글을 데이터베이스에 추가하고, 게시글 목록으로 리디렉션합니다.
            •	updateBoard() 메서드:
            •	게시글을 업데이트하고, 게시글 목록으로 리디렉션합니다.
            •	deleteBoard() 메서드:
            •	게시글을 삭제하고, 게시글 목록으로 리디렉션합니다.

    이 코드는 기본적인 게시판 기능을 제공하는 서블릿 컨트롤러를 구현합니다.
    필요한 경우 추가적인 기능을 구현하여 확장할 수 있습니다. 뷰 파일(.jsp)은 별도로 구현하여 데이터 표현을 담당하게 됩니다.

}

package com.example.controller;

import com.example.dao.BoardDAO;
import com.example.dao.DAOException;
import com.example.model.Board;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * BoardController is a servlet that handles requests for board operations
 * including listing, adding, and deleting board posts.
 */
@WebServlet("/board")
public class BoardController extends HttpServlet {
    private BoardDAO boardDAO;

    @Override
    public void init() throws ServletException {
        // Initialize the DAO with database connection details
        String url = getServletContext().getInitParameter("DB_URL");
        String user = getServletContext().getInitParameter("DB_USER");
        String password = getServletContext().getInitParameter("DB_PASSWORD");
        boardDAO = new BoardDAO(url, user, password);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Determine the action based on request parameters
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "list":
                    listBoards(request, response);
                    break;
                case "view":
                    viewBoard(request, response);
                    break;
                case "delete":
                    deleteBoard(request, response);
                    break;
                default:
                    listBoards(request, response);
                    break;
            }
        } catch (DAOException e) {
            throw new ServletException("Error handling board operation", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form submission for adding or updating a board post
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addBoard(request, response);
                    break;
                case "update":
                    updateBoard(request, response);
                    break;
                default:
                    listBoards(request, response);
                    break;
            }
        } catch (DAOException e) {
            throw new ServletException("Error handling board operation", e);
        }
    }

    private void listBoards(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve all board posts and forward to the list view
        List<Board> boards = boardDAO.getAllBoards();
        request.setAttribute("boards", boards);
        request.getRequestDispatcher("/WEB-INF/views/boardList.jsp").forward(request, response);
    }

    private void viewBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve a specific board post and forward to the detail view
        int id = Integer.parseInt(request.getParameter("id"));
        Board board = boardDAO.getBoardById(id);
        request.setAttribute("board", board);
        request.getRequestDispatcher("/WEB-INF/views/boardView.jsp").forward(request, response);
    }

    private void addBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Create a new board post and redirect to the list view
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String author = request.getParameter("author");
        Board board = new Board(title, content, author);
        boardDAO.addBoard(board);
        response.sendRedirect("board?action=list");
    }

    private void updateBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Update an existing board post and redirect to the list view
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String author = request.getParameter("author");
        Board board = new Board(id, title, content, author);
        boardDAO.updateBoard(board);
        response.sendRedirect("board?action=list");
    }

    private void deleteBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Delete a board post and redirect to the list view
        int id = Integer.parseInt(request.getParameter("id"));
        boardDAO.deleteBoard(id);
        response.sendRedirect("board?action=list");
    }
}