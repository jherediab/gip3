package com.ssl.jv.gip.negocio.dao;

import java.util.List;
import javax.ejb.Local;
import com.ssl.jv.gip.jpa.pojo.AgenteAduana;

@Local
public interface AgenteAduanaDAOLocal {

	public List<AgenteAduana> consultarAgenteAduanaPorFiltro(AgenteAduana pFiltro);
	
}