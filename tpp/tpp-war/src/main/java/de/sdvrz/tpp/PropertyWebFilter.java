/*
 * Copyright 2018 SDV-IT, Sparda Datenverarbeitung eG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.sdvrz.tpp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to set Header properties into HTTP Response
 *
 */
@WebFilter("/*")
public class PropertyWebFilter implements Filter
{   
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
         IOException
   {
      request.setCharacterEncoding("UTF-8");
      ((HttpServletResponse)response).setDateHeader("Expires", System.currentTimeMillis() + 604800000L);
      ((HttpServletResponse)response).setHeader("Pragma", null);
      ((HttpServletResponse)response).setHeader("Cache-Control", "public");
      chain.doFilter(request, response);
   }

   @Override
   public void init(FilterConfig filterConfig) throws ServletException
   {

   }

   @Override
   public void destroy()
   {

   }

}
