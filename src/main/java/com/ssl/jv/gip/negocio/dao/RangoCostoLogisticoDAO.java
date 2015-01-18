package com.ssl.jv.gip.negocio.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ssl.jv.gip.jpa.pojo.RangoCostoLogistico;

@Stateless
@LocalBean
public class RangoCostoLogisticoDAO extends GenericDAO<RangoCostoLogistico> implements RangoCostoLogisticoDAOLocal{

	private static final Logger LOGGER = Logger.getLogger(RangoCostoLogisticoDAO.class);
	
	public RangoCostoLogisticoDAO(){
		this.persistentClass = RangoCostoLogistico.class;
	}	

}
