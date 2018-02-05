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
		 * getParametr()�ǻ�ȡ�������ֵ�ķ���,�������ǻ�ȡaction�Ĳ���ֵ,����Parameterֻ��get����û��post����
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
		 * ���ݲ���ֵ�Ĳ�ͬ�����жϵ�����صķ���
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
			String header = "����,���,��ע,ʱ��";
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
				if(account.isEmpty()) {//�����ر�ע�ⲻȻ�����������
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
	 * get��post������:get�İ�ȫ�Ը�����Ϊget�����ڽ���ҳ����תʱ����url�д�����صĴ��ݲ�������post���ַ������ᰲȫ�Ը���
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
		req.setAttribute("card", card);//������ͨ����ֵ�Ե���ʽ�洢��ص���ֵ,����Attribute������set����Ҳ��get����������Ժ�Parameter��������
		req.getRequestDispatcher("/WEB-INF/pag/success.jsp").forward(req, resp);
		/*
		 * ������	req.getRequestDispatcher("��ַ").forward(req, resp)������ת����Ӧ��ҳ��,����ĵ�ַ�������Ĭ�ϵ�ַ�����·��
		 * ��������ǰ�˲��ֵĴ��붼д��webapp����,֮ǰ��MyEclipse�ǲ����ĵط����������ڸ㶮��
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
			Fenye list = service.List(number, password, Integer.parseInt(currentPage));//��ȡ��¼
			req.setAttribute("fenye", list);//�����¼
			req.setAttribute("username", username);
			req.setAttribute("password", password);//����֮���Ա���number��password��Ϊ��ǰ��ҳ����ʾ������
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
		javax.servlet.http.HttpSession session = req.getSession();//����н��и�ֵ����ԭ����Session�����û�д����µ�Session
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
	 * Servlet��������ǵ��ú�̨����ش�����к�̨�����о��ǻ�ȡ������ǰ��ҳ�����ز����Լ���ת��ַ
	 */
}
