package inter;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import BankCard.Card;

public interface CardDAO {
	public Card Get(@Param("username")String username,@Param("password")String password);
	/*
	 * �����DAO�����ǰJDBCʱ��һ����������Ϊ�ӿڣ�ֻ��Ҫ���巽���Ϳ���,ͬʱ��Ҫע�������@Param()�����Ǹ�������ע��
	 */
	public int modifyPassword(@Param("number")int number,@Param("newPassword")String newPassword);//�������Ҫ�����
	public int modifyMoney(@Param("number")int number,@Param("money")double money);
	public int open(Card card);
	
	public ArrayList<Card> List(@Param("username") String username,@Param("currentNumber")int currentNumber,@Param("move")int move);
	public int totalNumber(String username);
	public Card GetCard(int number);
	public int  delete(int number);
}