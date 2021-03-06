
/* 
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron, 
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND 
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE 
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR 
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. 
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION, 
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS 
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY 
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */

package org.csstudio.websuite.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.csstudio.websuite.epics.IocRequester;

/**
 * @author Markus Moeller
 *
 */
public class IocViewServlet extends HttpServlet {
    
    /** Generated serial version id */
    private static final long serialVersionUID = -3099724878599407478L;
    
    private IocRequester iocReq;
    
    @Override
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        iocReq = new IocRequester();
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
        this.createPage(request, response);
    }
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
        this.createPage(request, response);
    }

    private void createPage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        StringBuilder page = null;
        String iocName = null;
        
        page = new StringBuilder();
        page.append("<html>\n");
        page.append("<head>\n");
        page.append("<title>IocViewer</title>\n");
        page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/style/webviewer.css\">");
        page.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">\n");
        page.append("<meta http-equiv=\"Refresh\" content=\"31\">\n");
        page.append("</head>\n");
        page.append("<body>\n");
        
        page.append("<div class=\"reloadtime\">Reload every 31 seconds.</div><p>\n");
        
        this.appendRufbereitschaftRow(page);
        
        page.append("<table style=\"border-width:medium; border-style:double;\">\n");
        
        iocName = request.getParameter("ioc");
        if(iocName == null) {
            page.append("<tr>\n");
            page.append("<td colspan=\"2\"><font color=\"#ff0000\"><b>ERROR:</b> The URL does not contain the IOC name.</font></td>\n");
            page.append("</tr>\n");
        } else if(iocName.length() == 0) {
            page.append("<tr>\n");
            page.append("<td colspan=\"2\"><font color=\"#ff0000\"><b>ERROR:</b> The URL does not contain the IOC name.</font></td>\n");
            page.append("</tr>\n");
        } else {
            // Check the trailing and leading characters
            if(iocName.startsWith("~")) {
                iocName = iocName.substring(1);
            }
            
            if(iocName.endsWith("~")) {
                iocName = iocName.substring(0, iocName.length() - 1);
            }
            
            this.appendIOCNameRow(page, iocName);
            this.appendLineRow(page);
            this.appendDescRow(page, iocName);
            this.appendBootTimeRow(page, iocName);
            this.appendIOCAliveRow(page, iocName);
            this.appendIOCRedundantRow(page, iocName);
            this.appendButtonRow(request, iocName, page);
        }

        page.append("</table>\n");
        page.append("</body>\n</html>");

        response.getOutputStream().print(page.toString());
        response.getOutputStream().flush();
    }
        
    private void appendRufbereitschaftRow(StringBuilder page) {
        page.append("<p>\n");
        page.append("<div class=\"rufbereitschaft\">Rufbereitschaft hat: ");
        page.append(iocReq.askOnCallDuty() + "</div><br>\n");
    }
    
    private void appendIOCNameRow(StringBuilder page, String iocName)
    {
        page.append("<tr>\n");
        page.append("<th colspan=\"2\" align=\"center\">" + iocName + "</th>\n");
        page.append("</tr>\n");
    }

    private void appendIOCAliveRow(StringBuilder page, String iocName) {
        page.append("<tr>\n");
        page.append("<td><b>Alive:</b></td>\n");
        if(iocReq.isIocAlive(iocName)) {
            page.append("<td align=\"left\"><img class=\"ledon\" src=\"/images/null.gif\"></td>\n");
        } else {
            page.append("<td align=\"left\"><img class=\"noioc\" src=\"/images/null.gif\"></td>\n");
        }
        page.append("</tr>\n");
    }
    
    private void appendIOCRedundantRow(StringBuilder page, String iocName) {
        boolean stateA = iocReq.askFirstRedundantIoc(iocName);
        if (!stateA) {
            return;
        }
        boolean stateB = iocReq.askSecondRedundantIoc(iocName);
        page.append("<tr>\n");
        page.append("<td><b>Redundancy:</b></td>\n");
        page.append("<td align=\"left\">");
        if (stateA) {
            page.append("IOC A&nbsp;&nbsp;<img class=\"ledon\" src=\"/images/null.gif\">&nbsp;&nbsp;&nbsp;&nbsp;");
        } else {
            page.append("IOC A&nbsp;&nbsp;<img class=\"noioc\" src=\"/images/null.gif\">&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        if (stateB) {
            page.append("IOC B&nbsp;&nbsp;<img class=\"redundant\" src=\"/images/null.gif\">");
        } else {
            page.append("IOC B&nbsp;&nbsp;<img class=\"noioc\" src=\"/images/null.gif\">");
        }
        page.append("</td>\n");
        page.append("</tr>\n");
    }

    private void appendDescRow(StringBuilder page, String iocName) {
        page.append("<tr>\n");
        page.append("<td><b>Description:</b></td>\n");
        page.append("<td>" + iocReq.askIocDescription(iocName) + "</td>\n");
        page.append("</tr>\n");
    }
    
    private void appendBootTimeRow(StringBuilder page, String iocName) {
        page.append("<tr>\n");
        page.append("<td><b>Boot Time:</b></td>\n");
        page.append("<td>" + iocReq.askIocBootTime(iocName) + "</td>\n");
        page.append("</tr>\n");
    }
    
    private void appendLineRow(StringBuilder page) {
        page.append("<tr>\n<td colspan=\"2\"><hr></td>\n</tr>\n");
    }
    
    private void appendButtonRow(HttpServletRequest request, String iocName, StringBuilder page) {
        appendLineRow(page);
        page.append("<tr>\n");
        page.append("<form action=\"" + request.getRequestURL().toString() + "?ioc=" + iocName + "\" method=\"post\">\n");
        page.append("<td colspan=\"2\" align=\"center\" valign=\"middle\"><input class=\"button\" type=\"submit\" value=\"Reload\"></td>\n");
        page.append("</form>\n");
        page.append("</tr>\n");
    }
}
