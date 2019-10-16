package com.example.hello.hello;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.example.hello.hello.model.*;
import com.example.hello.hello.model.base.BaseResponse;
import com.example.hello.hello.model.base.ExceptionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@SpringBootApplication
@RestController
public class DemoApplication implements ErrorController {

	static List<ExchangeCurrency> tradeList = new ArrayList<>();

	@Autowired
	private ErrorAttributes errorAttributes;

	@GetMapping(value = "/hello")
	public ResponseEntity<List<ExchangeCurrency>> getMethodName() {
		// List<ExchangeCurrency> currencyList = new ArrayList<ExchangeCurrency>();
		this.setup();
		// ExchangeCurrency currency = new ExchangeCurrency("MYR", "Malaysia Ringgit",
		// new BigDecimal(0.238582));
		// currency.setCode("SGD");
		// currency.setName("Singapore Dollar");

		// currencyList.add(currency);

		return ResponseEntity.ok().body(tradeList);
	}

	@RequestMapping(value = "/exchange", method = RequestMethod.GET)
	public ResponseEntity<String> exhange(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("amount") BigDecimal amount) {

		this.setup();

		try {
			String fromCurrencyCode = Currency.getInstance(from.toUpperCase()).getCurrencyCode();
			String toCurrency = Currency.getInstance(to.toUpperCase()).getCurrencyCode();

			ExchangeCurrency fCurrencyModel = null;
			ExchangeCurrency tCurrencyModel = null;

			for (ExchangeCurrency currencyModel : tradeList) {
				if (currencyModel.getCode().equalsIgnoreCase(fromCurrencyCode)) {
					fCurrencyModel = currencyModel;
				}

				if (currencyModel.getCode().equalsIgnoreCase(toCurrency)) {
					tCurrencyModel = currencyModel;
				}
			}

			if (fCurrencyModel != null && tCurrencyModel != null) {
				return new ResponseEntity<>(fCurrencyModel.exhange(tCurrencyModel, amount), HttpStatus.OK);
			}

		} catch (IllegalArgumentException illegalArgumentException) {
			return new ResponseEntity<>("Invalid currency Code", HttpStatus.BAD_REQUEST);
		} catch (NullPointerException nullPointerException) {
			return new ResponseEntity<>("invalid operation", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Rate not set for this currency", HttpStatus.OK);
	}

	@RequestMapping(value = "/exchangeRate", method = RequestMethod.GET)
	public ResponseEntity<String> exchange(@RequestParam("from") String from, @RequestParam("to") String to) {

		this.setup();

		try {
			String fromCurrencyCode = Currency.getInstance(from.toUpperCase()).getCurrencyCode();
			String toCurrency = Currency.getInstance(to.toUpperCase()).getCurrencyCode();

			ExchangeCurrency fCurrencyModel = null;
			ExchangeCurrency tCurrencyModel = null;

			for (ExchangeCurrency currencyModel : tradeList) {
				if (currencyModel.getCode().equalsIgnoreCase(fromCurrencyCode)) {
					fCurrencyModel = currencyModel;
				}

				if (currencyModel.getCode().equalsIgnoreCase(toCurrency)) {
					tCurrencyModel = currencyModel;
				}
			}

			if (fCurrencyModel != null && tCurrencyModel != null) {
				return new ResponseEntity<>(fCurrencyModel.exhange(tCurrencyModel, new BigDecimal(1)), HttpStatus.OK);
			}

		} catch (IllegalArgumentException illegalArgumentException) {

			return new ResponseEntity<>("Invalid currency Code", HttpStatus.BAD_REQUEST);
		} catch (NullPointerException nullPointerException) {
			return new ResponseEntity<>("invalid operation", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Rate not set for this currency", HttpStatus.OK);
	}

	@RequestMapping(value = "/exchange/confirm", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> confirmExhange(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("amount") BigDecimal amount) {

		BaseResponse response = new BaseResponse();
		this.setup();

		try {
			String fromCurrencyCode = Currency.getInstance(from.toUpperCase()).getCurrencyCode();
			String toCurrency = Currency.getInstance(to.toUpperCase()).getCurrencyCode();

			ExchangeCurrency fCurrencyModel = null;
			ExchangeCurrency tCurrencyModel = null;

			for (ExchangeCurrency currencyModel : tradeList) {
				if (currencyModel.getCode().equalsIgnoreCase(fromCurrencyCode)) {
					fCurrencyModel = currencyModel;
				}

				if (currencyModel.getCode().equalsIgnoreCase(toCurrency)) {
					tCurrencyModel = currencyModel;
				}
			}

			if (fCurrencyModel != null && tCurrencyModel != null) {
				storeInDatabase(fCurrencyModel, tCurrencyModel, amount);

				response.setErrorCode(0);
				response.setErrorMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (IllegalArgumentException illegalArgumentException) {
			response.setErrorCode(9999);
			response.setErrorMessage("Invalid currency Code");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (NullPointerException nullPointerException) {
			response.setErrorCode(9999);
			response.setErrorMessage("Invalid operation");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			response.setErrorCode(9999);
			response.setErrorMessage(e.toString());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setErrorCode(9999);
		response.setErrorMessage("Rate not set for this currency");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public void setup() {

		if (tradeList.size() == 0) {
			ExchangeCurrency usd = new ExchangeCurrency("USD", "US Dollar", new BigDecimal(1.0000));
			ExchangeCurrency sgd = new ExchangeCurrency("SGD", "Singapore Dollar", new BigDecimal(0.729485));
			ExchangeCurrency myr = new ExchangeCurrency("MYR", "Malaysian Ringgit", new BigDecimal(0.238655));
			ExchangeCurrency idr = new ExchangeCurrency("IDR", "Indonesian Rupiah", new BigDecimal(0.0000706001));
			ExchangeCurrency eur = new ExchangeCurrency("EUR", "Euro", new BigDecimal(1.10180));

			tradeList.add(usd);
			tradeList.add(sgd);
			tradeList.add(myr);
			tradeList.add(idr);
			tradeList.add(eur);
		}
	}

	public void storeInDatabase(ExchangeCurrency fromCurrency, ExchangeCurrency toCurrency, BigDecimal amount)
			throws IOException {
		File file = ResourceUtils.getFile("classpath:result.csv");

		FileWriter fr = new FileWriter(file, true);
		BufferedWriter br = new BufferedWriter(fr);

		br.append("\n");
		br.append(fromCurrency.getCode() + "," + toCurrency.getCode() + "," + amount.toString() + ","
				+ fromCurrency.exhange(toCurrency, new BigDecimal(1)) + "," + fromCurrency.exhange(toCurrency, amount));
		br.close();
		fr.close();

		// File is found
		System.out.println("File Found : " + file.exists());

		// Read File Content
		String content = new String(Files.readAllBytes(file.toPath()));
		System.out.println(content);

	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public void setErrorAttributes(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping(value = "error")
	@ResponseBody
	public ExceptionResponse error(WebRequest webRequest, HttpServletResponse response) {
		return new ExceptionResponse(response.getStatus(), getErrorAttributes(webRequest));
	}

	@Override
	public String getErrorPath() {
		return "error";
	}

	private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.putAll(errorAttributes.getErrorAttributes(webRequest, false));
		return errorMap;
	}

}
