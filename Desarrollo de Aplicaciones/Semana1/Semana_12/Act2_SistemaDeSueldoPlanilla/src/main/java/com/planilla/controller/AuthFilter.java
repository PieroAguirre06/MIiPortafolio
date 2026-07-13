/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String loginURI = request.getContextPath() + "/login.jsp";
        String loginServlet = request.getContextPath() + "/login";
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);
        boolean loginRequest = request.getRequestURI().equals(loginURI) || request.getRequestURI().equals(loginServlet);
        boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + "/imagenes/") ||
                                   request.getRequestURI().startsWith(request.getContextPath() + "/admin/") ||
                                   request.getRequestURI().startsWith(request.getContextPath() + "/rrhh/");

        if (loggedIn || loginRequest || resourceRequest) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(loginURI);
        }
    }
}