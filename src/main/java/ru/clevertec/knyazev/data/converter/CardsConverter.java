package ru.clevertec.knyazev.data.converter;

import java.util.HashSet;
import java.util.Set;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;

public class CardsConverter implements Converter<Set<DiscountCardDTO>, String[]> {
	private static final String CARD_PREFIX = "card-";

	@Override
	public Set<DiscountCardDTO> convert(String[] cardsData) throws ConverterException {
		Set<DiscountCardDTO> discountCardsDTO = new HashSet<>();

		if ((cardsData != null) && (cardsData.length > 0)) {
			if ((cardsData.length == 1) && (cardsData[0].equals("") || cardsData[0].equals(" "))) {
				return discountCardsDTO;
			}
			
			for (int i = 0; i < cardsData.length; i++) {
				if (cardsData[i].contains(CARD_PREFIX) && cardsData[i].startsWith(CARD_PREFIX)) {
					String cardNumber = cardsData[i].substring(CARD_PREFIX.length());
					discountCardsDTO.add(new DiscountCardDTO(cardNumber));
				} else {
					throw new ConverterException("Error in card number format");
				}
			}
		}

		return discountCardsDTO;
	}
}