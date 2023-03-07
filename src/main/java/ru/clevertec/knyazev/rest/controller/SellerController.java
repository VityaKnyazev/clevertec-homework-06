package ru.clevertec.knyazev.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.data.validator.SellerValidator;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.SellerService;
import ru.clevertec.knyazev.service.exception.ServiceException;

@Controller
public class SellerController {
	private SellerService sellerServiceImpl;
	private SellerValidator sellerValidator = new SellerValidator();

	@Autowired
	public SellerController(SellerService sellerServiceImpl) {
		this.sellerServiceImpl = sellerServiceImpl;
	}
	
	/** 
	 * For using JSON output undo committed strings and commit string below committed
	 */
	// @GetMapping(value = "/sellers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/sellers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public ResponseEntity<String> showSeller(@PathVariable Long id) {
		Optional<Seller> seller = sellerServiceImpl.showSeller(id);
//		String sellerResult = (seller.isEmpty()) ? "{}" : seller.get().toString();
		String sellerResult = (seller.isEmpty()) ? "" : seller.get().toXML();
		return new ResponseEntity<String>(sellerResult, HttpStatus.OK);
	}

	/** 
	 * For using JSON output undo committed strings and commit string below committed
	 */
//	@GetMapping(value = "/sellers", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/sellers", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public ResponseEntity<String> showAllSellers() {
		List<Seller> sellers = sellerServiceImpl.showAllSellers();

//		String sellerResult = (sellers.size() == 0) ? "[]"
//				: sellers.stream().map(seller -> seller.toString()).collect(Collectors.joining(",", "[", "]"));
		String sellerResult = (sellers.size() == 0) ? ""
				: sellers.stream().map(seller -> seller.toXML())
						.collect(Collectors.joining(System.lineSeparator(), "<sellers>", "</sellers>"));
//		sellerResult = sellerResult.replaceFirst(",\\]$", "]");
		return new ResponseEntity<String>(sellerResult, HttpStatus.OK);
	}

	@PostMapping(value = "/sellers", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> addSeller(@RequestBody Seller seller) {

		try {
			sellerValidator.validate(seller);
			sellerServiceImpl.addSeller(seller);
		} catch (ServiceException | ValidatorException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		String result = "Successfuly added" + System.lineSeparator() + seller.toString();
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@PutMapping(value = "/sellers", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> changeSeller(@RequestBody Seller seller) {

		try {
			sellerValidator.validate(seller);
			sellerServiceImpl.changeSeller(seller);
		} catch (ServiceException | ValidatorException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		String result = "Successfuly changed" + System.lineSeparator() + seller.toString();
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@DeleteMapping(value = "/sellers", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> removeSeller(@RequestBody Seller seller) {

		try {
			sellerValidator.validate(seller);
			sellerServiceImpl.removeSeller(seller);
		} catch (ServiceException | ValidatorException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		String result = "Successfuly removed";
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

}
