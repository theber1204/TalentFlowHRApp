package com.talentflow.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(urlPatterns = {"/*"})
public class FacesRewriteFilter implements Filter {

    private Map<String, String> urlMappings;

    @Override
    public void init(FilterConfig filterConfig) {
        // Creamos un mapa de redireccionamiento
        urlMappings = new HashMap<>();
        urlMappings.put("/dashboard", "/pages/dashboard.xhtml");
        urlMappings.put("/login", "/index.xhtml");
        urlMappings.put("/perfil", "/pages/perfil.xhtml");
        urlMappings.put("/vacantes", "/pages/vacantes.xhtml");
        urlMappings.put("/vacantes/crear", "/pages/vacantes/crear.xhtml");
        urlMappings.put("/candidatos", "/pages/candidatos/candidatos.xhtml");
        urlMappings.put("/registro", "/registro.xhtml");
        urlMappings.put("/registro-exitoso", "/registro-exitoso.xhtml");

        System.out.println("FacesRewrite: Filtro inicializado con " + urlMappings.size() + " mapeos");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI().substring(contextPath.length());

        // Ignorar recursos estáticos
        if (uri.contains("/javax.faces.resource/") ||
                uri.contains("/resources/") ||
                uri.contains(".js") ||
                uri.contains(".css") ||
                uri.contains(".png") ||
                uri.contains(".jpg") ||
                uri.contains(".gif")) {
            chain.doFilter(request, response);
            return;
        }

        // Buscar si hay una regla definida para esta URL
        String targetPage = urlMappings.get(uri);
        if (targetPage != null) {
            System.out.println("FacesRewrite: Redirigiendo " + uri + " a " + targetPage);
            request.getRequestDispatcher(targetPage).forward(request, response);
            return;
        }

        // Si no hay regla, continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        urlMappings.clear();
        urlMappings = null;
    }
}