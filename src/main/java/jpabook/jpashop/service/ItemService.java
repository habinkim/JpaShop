package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	@Transactional
	public void updateItem(Long id, String name, int price) {
		Item item = itemRepository.findOne(id);
		item.setName(name);
		item.setPrice(price);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}
