package ru.academits.gerasimenko.todolistservlet.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.academits.gerasimenko.todolistservlet.data.TodoItem;
import ru.academits.gerasimenko.todolistservlet.data.TodoItemsInMemoryRepository;
import ru.academits.gerasimenko.todolistservlet.data.TodoItemsRepository;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class TodoListServlet extends HttpServlet {
    private TodoItemsRepository todoItemsRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        todoItemsRepository = new TodoItemsInMemoryRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        req.removeAttribute("updateError");
        req.removeAttribute("updateErrorItemId");

        String createError = getErrorMessage(session, "createError");
        req.setAttribute("createError", createError);

        String findError = getErrorMessage(session, "findError");
        req.setAttribute("findError", findError);

        String updateError = getErrorMessage(session, "updateError");
        req.setAttribute("updateError", updateError);

        String updateErrorItemId = getErrorMessage(session, "updateErrorItemId");
        req.setAttribute("updateErrorItemId", updateErrorItemId);

        List<TodoItem> todoItems = todoItemsRepository.getAll();
        req.setAttribute("todoItems", todoItems);

        req.getRequestDispatcher("/todo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        switch (action) {
            case "create" -> {
                String text = req.getParameter("text");

                if (text == null || text.trim().isEmpty()) {
                    session.setAttribute("createError", "Необходимо задать текст заметки");
                } else {
                    todoItemsRepository.create(new TodoItem(text.trim()));
                }
            }

            case "save" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                String text = req.getParameter("text");


                if (text == null || text.trim().isEmpty()) {
                    session.setAttribute("updateError", "Необходимо задать текст заметки");
                    session.setAttribute("updateErrorItemId", id);
                } else {
                    try {
                        todoItemsRepository.update(new TodoItem(id, text));
                    } catch (IllegalArgumentException e) {
                        session.setAttribute("findError", "Не удалось найти заметку с указанным id");
                    }
                }
            }

            case "delete" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                todoItemsRepository.delete(id);
            }
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/");
    }

    private static String getErrorMessage(HttpSession session, String errorAttribute) {
        String errorMessage = session.getAttribute(errorAttribute) != null
                ? session.getAttribute(errorAttribute).toString()
                : "";
        session.removeAttribute(errorAttribute);

        return errorMessage;
    }
}