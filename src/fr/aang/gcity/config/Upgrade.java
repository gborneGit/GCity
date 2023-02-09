package fr.aang.gcity.config;

public class Upgrade {
	
	private	int	_price;
	private	int	_quantity;
	
	public Upgrade(int price, int quantity) {
		_price = price;
		_quantity = quantity;
	}
	
	public int price() {
		return _price;
	}
	
	public int quantity() {
		return _quantity;
	}
}
