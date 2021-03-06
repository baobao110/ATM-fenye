package inter;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import BankCard.Card;

public interface CardDAO {
	public Card Get(@Param("username")String username,@Param("password")String password);
	/*
	 * 这里的DAO层和以前JDBC时不一样，这里作为接口，只需要定义方法就可以,同时需要注意这里的@Param()这里是给参数加注解
	 */
	public int modifyPassword(@Param("number")int number,@Param("newPassword")String newPassword);//多个参数要起别名
	public int modifyMoney(@Param("number")int number,@Param("money")double money);
	public int open(Card card);
	
	public ArrayList<Card> List(@Param("username") String username,@Param("currentNumber")int currentNumber,@Param("move")int move);
	public int totalNumber(String username);
	public Card GetCard(int number);
	public int  delete(int number);
}
