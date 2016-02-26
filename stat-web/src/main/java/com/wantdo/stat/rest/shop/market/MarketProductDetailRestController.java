package com.wantdo.stat.rest.shop.market;

import com.wantdo.stat.beanvalidator.BeanValidators;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.ProductDetail;
import com.wantdo.stat.entity.front.vo.ProductDetailVo;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.market.ProductService;
import com.wantdo.stat.web.MediaTypes;
import com.wantdo.stat.web.Servlets;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.util.Map;

/**
 * 产品管理Restful
 *
 * @ Date : 2015-8-27
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/market/product_detail")
public class MarketProductDetailRestController {

    private static Logger logger = LoggerFactory.getLogger(MarketProductDetailRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public TableDTO<ProductDetailVo> getAllProduct(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                                                           @RequestParam(value = "order", defaultValue = "auto") String sortType,
                                                           @RequestParam(value = "search", defaultValue = "") String search,
                                                           @PathVariable("id") Long productId, Model model,
                                                           ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("LIKE_sku", search);

        int pageNumber = offset/pageSize + 1;

        TableDTO<ProductDetailVo> productDetails = productService.getProductDetailVo(productId, searchParams, pageNumber, pageSize, sortType);
        if (productDetails == null) {
            String message = "产品不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return productDetails;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void update(@ModelAttribute(value = "productDetail")ProductDetail productDetail){
        BeanValidators.validateWithException(validator, productDetail);
        productService.saveProductDetail(productDetail);
    }

    @ModelAttribute
    public void getProduct(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model, ServletRequest request) {
        if (id != -1) {
            model.addAttribute("productDetail", productService.getProductDetail(id));
        }
    }



    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
