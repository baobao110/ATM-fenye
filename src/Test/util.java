package Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.runner.Request;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import AccountFlow.Account;
import BankCard.Card;
import fenye.Fenye;
import inter.CardDAO;
import service.CardService;
import service.Service;
import service.UserService;
import user.User;

public class util extends HttpServlet {
	
	private Service service=new Service();
	
	private UserService userservice=new UserService();
	
	private CardService cardservice=new CardService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		String msg=req.getParameter("action");
		System.out.println(msg);
		/*
		 * getParametr()是获取请求参数值的方法,在这里是获取action的参数值,对于Parameter只有get方法没有post方法
		 */
		if(msg.equals("openAccount")) {
			openAccount(req,resp);
		}
		
		if(msg.equals("toOpenAccount")) {
			toOpenAccount(req,resp);
		}
		
		if(msg.equals("save")) {
			save(req,resp);
		}
		
		if(msg.equals("toSave")) {
			toSave(req,resp);
		}
		
		if(msg.equals("check")) {
			check(req,resp);
		}
		
		if(msg.equals("toCheck")) {
			toCheck(req,resp);
		}
		
		if(msg.equals("transfer")) {
			transfer(req,resp);
		}
		
		if(msg.equals("toTransfer")) {
			toTransfer(req,resp);
		}
		
		if(msg.equals("list")) {
			List(req,resp);
		}
		
		if (msg.equals("toList")) {
			toList(req, resp);
		}
		
		if (msg.equals("toChangePassword")) {
			toChangePassword(req,resp);
		}
		
		if(msg.equals("password")) {
			Password(req,resp);
		}
		
		if(msg.equals("toRegister")) {
			System.out.println("1");
			toRegister(req,resp);
		}
		
		if(msg.equals("register")) {
			Register(req,resp);
		}
		
		if(msg.equals("toLogin")) {
			toLogin(req,resp);
		}
		
		if(msg.equals("login")) {
			Login(req,resp);
		}
		
		if(msg.equals("toUsercenter")) {
			toUsercenter(req,resp);
		}
		
		if(msg.equals("toDelete")) {
			toDelete(req,resp);
		}
		
		if(msg.equals("delete")) {
			delete(req,resp);
		}
		
		if(msg.equals("showPicture")) {
			showPicture(req,resp);
		}
		
		if(msg.equals("showPicture")) {
			showPicture(req,resp);
		}
		
		if(msg.equals("load")) {
			load(req,resp);
		}
		
		if(msg.equals("down")) {
			down(req,resp);
		}
		
		/*
		 * 根据参数值的不同进行判断调用相关的方法
		 */
	}
	private void down(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		javax.servlet.http.HttpSession session = req.getSession(); 
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String password=req.getParameter("password");
		System.out.println(password);
		Card cad=service.Get(username,password);
		int number=cad.getNumber();
		String filename = number + ".csv";
		resp.setContentType("application/octet-stream");  
		resp.setHeader("Content-Disposition", "attachment;filename="+ filename);  
		
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()))) {
			int currentPage = 1;
			String header = "卡号,金额,备注,时间";
			bw.write(header);
			bw.newLine();
			bw.flush();
			while (true) {
				Fenye list = service.List(number, password, currentPage);
				System.out.println(currentPage);
				 ArrayList<Account>account=(ArrayList<Account>) list.getObject();
				if(null==account) {
					break;
				}
				if(account.isEmpty()) {//这里特别注意不然不能下载完成
					break;
				}
				for(Account i:account) {
					StringBuilder text = new StringBuilder();
					text.append(i.getNumber()).append(",")
					.append(i.getMoney()).append(",")
					.append(i.getDescription()).append(",")
					.append(i.getCreatetime()).append(",");
					bw.write(text.toString());
					bw.newLine();
					bw.flush();
					}
				currentPage++;
			}
		}
	}

	private void load(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
		factory.setSizeThreshold(10240);
		
		String src = req.getServletContext().getRealPath("/");
		
		factory.setRepository(new File(src + "WEB-INF/load-tmp"));

		// Create a new file upload handler
		ServletFileUpload load = new ServletFileUpload(factory);

		// Set overall request size constraint
		load.setSizeMax(1024 * 500);

		// Parse the request
		try {
			List<FileItem> items = load.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = iter.next();

			    if (item.isFormField()) {
			    	 String name = item.getFieldName();
			    	 String value = item.getString();
			    } else {
			    	 HttpSession session = req.getSession();
			    	 User user = (User)session.getAttribute("user");
			    	 String fileName = "" + user.getUsername();
			    	 File loadedFile = new File(src + "WEB-INF/load/" +  fileName);
			    	 item.write(loadedFile);
			    }
			}
			toUsercenter(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * get和post的区别:get的安全性更低因为get方法在进行页面跳转时会在url中带有相关的传递参数但是post这种方法不会安全性更高
	 */
	
	private void showPicture(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		String src=req.getServletContext().getRealPath("/");
		HttpSession session=req.getSession();
		User user=(User)session.getAttribute("user");
		try (FileInputStream in = new FileInputStream(src+ "WEB-INF/load/" + user.getUsername());
				OutputStream out = resp.getOutputStream()) {
			byte[] data = new byte[1024];
			int length = -1;
			while((length=in.read(data))!=-1) {
				out.write(data, 0, length);
				out.flush();
			}
		}
	}

	private void toDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/delete.jsp").forward(req, resp);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession(); 
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String password=req.getParameter("password");
		Card cad=service.Get(username,password);
		int number=cad.getNumber();
		service.delete(number);
		session.setAttribute("user",user);
		toUsercenter(req, resp);
	}
	
	private void openAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String password=req.getParameter("password");
		Card card=service.open(username,password);
		req.setAttribute("card", card);//这里是通过键值对的形式存储相关的数值,对于Attribute属性有set方法也有get方法这里可以和Parameter参数区别
		req.getRequestDispatcher("/WEB-INF/pag/success.jsp").forward(req, resp);
		/*
		 * 这里用	req.getRequestDispatcher("地址").forward(req, resp)方法跳转到相应的页面,这里的地址是相对于默认地址的相对路径
		 * 所以属于前端部分的代码都写在webapp下面,之前用MyEclipse是不理解的地方在这里终于搞懂了
		 */
	}
	
	private void toOpenAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		req.getRequestDispatcher("/WEB-INF/pag/OpenAccount.jsp").forward(req, resp);
	}
	
	private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession(); 
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String password=req.getParameter("password");
		String b=req.getParameter("money");
		double money=Double.parseDouble(b);
		Card cad=service.Get(username,password);
		int number=cad.getNumber();
		service.save(number, password, money);
		Card card=service.GetCard(number);
		req.setAttribute("card", card);
		req.getRequestDispatcher("/WEB-INF/pag/success.jsp").forward(req, resp);
	}
	
	private void toSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/save.jsp").forward(req, resp);
	}
	
	private void transfer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession(); 
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String password=req.getParameter("password");
		String b=req.getParameter("money");
		double money=Double.parseDouble(b);
		Card cad=service.Get(username,password);
		int OutNumber=cad.getNumber();
		String c=req.getParameter("InNumber");
		int InNumber=Integer.parseInt(c);
		service.transfer(OutNumber, password, money, InNumber);
		Card card1=service.GetCard(OutNumber);
		Card card2=service.GetCard(InNumber);
		req.setAttribute("card1", card1);
		req.setAttribute("card2", card2);
		req.getRequestDispatcher("/WEB-INF/pag/success2.jsp").forward(req, resp);
	}
	
	private void toTransfer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/transfer.jsp").forward(req, resp);
	}
	
	private void check(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String password=req.getParameter("password");
		String b=req.getParameter("money");
		double money=Double.parseDouble(b);
		Card cad=service.Get(username,password);
		int number=cad.getNumber();
		service.draw(number, password, money);
		Card card=service.GetCard(number);
		req.setAttribute("card", card);
		req.getRequestDispatcher("/WEB-INF/pag/success.jsp").forward(req, resp);
	}
	
	private void toCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/check.jsp").forward(req, resp);
	}
	
	private void List(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			javax.servlet.http.HttpSession session = req.getSession();
			User user=(User) session.getAttribute("user");
			String username=user.getUsername();
			String password=req.getParameter("password");
			Card cad=service.Get(username,password);
			int number=cad.getNumber();
			String currentPage = req.getParameter("currentPage");
			Fenye list = service.List(number, password, Integer.parseInt(currentPage));//获取记录
			req.setAttribute("fenye", list);//保存记录
			req.setAttribute("username", username);
			req.setAttribute("password", password);//这里之所以保存number和password是为了前端页面显示的作用
			req.getRequestDispatcher("/WEB-INF/pag/list.jsp").forward(req, resp);
		}
	
	private void toList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/list.jsp").forward(req, resp);
	
	}
	
	private void toChangePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/password.jsp").forward(req, resp);
	
	}
	
	private void Password(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String oldPassword= req.getParameter("oldPassword");
		Card cad=service.Get(username,oldPassword);
		int number=cad.getNumber();
		String newPassword= req.getParameter("newPassword");
		Card card=service.ChangePassword(number, oldPassword, newPassword);
		req.setAttribute("card", card);
		req.getRequestDispatcher("/WEB-INF/pag/success.jsp").forward(req, resp);
	}
	
	private void toRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		req.getRequestDispatcher("/WEB-INF/pag/register.jsp").forward(req, resp);
	
	}
	
	private void Register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();//如果有进行赋值就是原来的Session，如果没有创建新的Session
		String username= req.getParameter("username");
		String psssword= req.getParameter("password");
		User user= userservice.register(username,psssword);
		session.setAttribute("user",user);
		req.getRequestDispatcher("/WEB-INF/pag/login.jsp").forward(req, resp);
	}
	
	private void toLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		req.getRequestDispatcher("/WEB-INF/pag/login.jsp").forward(req, resp);																																									
	}

	private void Login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		String username= req.getParameter("username");
		String psssword= req.getParameter("password");
		User user=userservice.login(username, psssword);
		session.setAttribute("user", user);
		toUsercenter(req, resp);
	}
	
	private void toUsercenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		User user=(User) session.getAttribute("user");
		String username=user.getUsername();
		String currentPage = req.getParameter("currentPage");
		currentPage=currentPage ==null ? "1" : currentPage;
		Fenye list=cardservice.List(username, Integer.parseInt(currentPage));
		req.setAttribute("fenye", list);
		req.setAttribute("currentPage", currentPage);
		req.setAttribute("username", username);
		req.getRequestDispatcher("/WEB-INF/pag/usercenter.jsp").forward(req, resp);
	}
	
	/*
	 * Servlet类的作用是调用后台的相关代码进行后台处理还有就是获取和设置前端页面的相关参数以及跳转地址
	 */
}
