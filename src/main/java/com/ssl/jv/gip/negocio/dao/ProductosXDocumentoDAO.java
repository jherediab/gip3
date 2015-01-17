package com.ssl.jv.gip.negocio.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.ssl.jv.gip.jpa.pojo.ProductosXDocumento;
import com.ssl.jv.gip.negocio.dto.ProductoFacturaDirectaDTO;

/**
 * Session Bean implementation class ProductosXDocumentoDAO
 */
@Stateless
@LocalBean
public class ProductosXDocumentoDAO extends GenericDAO<ProductosXDocumento>
		implements ProductosXDocumentoDAOLocal {

	private static final Logger LOGGER = Logger
			.getLogger(ProductosXDocumentoDAO.class);

	/**
	 * @see GenericDAO#GenericDAO()
	 */
	public ProductosXDocumentoDAO() {
		this.persistentClass = ProductosXDocumento.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductosXDocumento> consultarPorDocumento(Long id) {
		Query query = em
				.createNamedQuery(ProductosXDocumento.FIND_BY_DOCUMENTO);
		query.setParameter("idDocumento", id);
		return query.getResultList();
	}
	
	
	
	public List<ProductoFacturaDirectaDTO> consultarProductoFacturaDirecta(String strConsecutivoDocumento) 
	{
		
		List<ProductoFacturaDirectaDTO> lista = new ArrayList<ProductoFacturaDirectaDTO>();
		
		try {
			
			
			String query="select pi.sku, pi.nombre ,u.abreviacion ,pxd.cantidad1,pxd.valor_unitatrio_ml,pxd.descuentoxproducto, pxd.valor_total,pxd.otros_descuentos,pxd.iva"
			+ " from productosxdocumentos pxd inner join documentos d on pxd.id_documento=d.id" 
			+ " inner join productos_inventario pi on pi.id=pxd.id_producto"
			+ " inner join unidades u on u.id=pi.id_ud"
			+ " where d.consecutivo_documento='"+strConsecutivoDocumento+"'";

			
			
			
			List<Object[]> listado = em.createNativeQuery(query).getResultList();
			
			
			System.out.println("query PXD LE Query"+query);
			System.out.println("query PXD LE"+listado.size());
			if (listado != null) {
				for (Object[] objs : listado) {
					ProductoFacturaDirectaDTO dto = new ProductoFacturaDirectaDTO();
					//ClienteDTO clienteDTO = new ClienteDTO();
					dto.setSku(objs[0] != null ? objs[0].toString() : null);
					dto.setNombre(objs[1] != null ? objs[1].toString() : null);
					dto.setUnidad(objs[2] != null ? objs[2].toString() : null);
					dto.setCantidad(objs[3] != null ? new BigDecimal(objs[3].toString()) : null);
					
					dto.setPrecio(objs[4] != null ? new BigDecimal(objs[4].toString()) : null);
					dto.setValorDescuento(objs[5] != null ? new BigDecimal(objs[5].toString()) : null);
					dto.setValorTotal(objs[6] != null ? new BigDecimal(objs[6].toString()) : null);
					dto.setValorOtrosDescuento(objs[7] != null ? new BigDecimal(objs[7].toString()) : null);
					dto.setValorIva(objs[8] != null ? new BigDecimal(objs[8].toString()) : null);
					
									lista.add(dto);
				}
			}
			return lista;
			
			
			
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
		
		
		return lista;
		
	}

}
