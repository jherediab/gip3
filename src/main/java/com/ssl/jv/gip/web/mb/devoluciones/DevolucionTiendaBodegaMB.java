package com.ssl.jv.gip.web.mb.devoluciones;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.ssl.jv.gip.jpa.pojo.Documento;
import com.ssl.jv.gip.jpa.pojo.Ubicacion;
import com.ssl.jv.gip.negocio.ejb.DevolucionesEJBLocal;
import com.ssl.jv.gip.negocio.ejb.MaestrosEJBLocal;
import com.ssl.jv.gip.web.mb.AplicacionMB;
import com.ssl.jv.gip.web.mb.MenuMB;
import com.ssl.jv.gip.web.mb.UtilMB;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * <p>
 * Title: GIP
 * </p>
 * <p>
 * Description: GIP
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Soft Studio Ltda.
 * </p>
 *
 * @author Diego Poveda
 * @email dpoveda@gmail.com
 * @phone 3192594013
 * @version 1.0
 */
@ManagedBean(name = "devolucionTiendaBodegaMB")
@ViewScoped
public class DevolucionTiendaBodegaMB extends UtilMB {

  /**
   *
   */
  private static final long serialVersionUID = -2780795923623719268L;

  private static final Logger LOGGER = Logger.getLogger(DevolucionTiendaBodegaMB.class);

  @EJB
  private DevolucionesEJBLocal devolucionesEJB;

  @EJB
  private MaestrosEJBLocal maestrosEJB;

  @ManagedProperty(value = "#{aplicacionMB}")
  private AplicacionMB appMB;

  @ManagedProperty(value = "#{menuMB}")
  private MenuMB menu;

  private final Integer language = AplicacionMB.SPANISH;
  /**
   * listado de ubicaciones
   */
  private List<Ubicacion> listaUbicaciones;
  /**
   * Mapa q almacena las ubicaciones para acceder a ellas mucho mas rapido.
   */
  private Map<Long, Ubicacion> ubicaciones;
  /**
   * tienda origen
   */
  private Long idUbicacionOrigen;
  /**
   * bodega de destino
   */
  private Ubicacion ubicacionOrigenSeeccionada;
  /**
   * lista de documentos que retorna la consulta a la base de datos
   */
  private List<Documento> listaDocumentos;
  /**
   * lista de documentos seleccionados
   */
  private List<Documento> listaDocumentosSeleccionados;

  @PostConstruct
  public void init() {
    LOGGER.debug("Metodo: <<init>>");
    cargarListaUbicaciones();
  }

  /**
   *
   */
  private void cargarListaUbicaciones() {
    LOGGER.debug("Metodo: <<cargarListaUbicaciones>>");
    setListaUbicaciones(devolucionesEJB.consultarUbicacionesQueSonTiendaPorUsuario(menu.getUsuario().getId()));
    if (getListaUbicaciones() != null) {
      setUbicacionOrigenSeeccionada(getListaUbicaciones().get(0));
      ubicaciones = new HashMap<>();
      for (Ubicacion ubicacion : getListaUbicaciones()) {
        ubicaciones.put(ubicacion.getId(), ubicacion);
      }
    }
  }

  /**
   *
   */
  public void onUbicacionOrigenChange() {
    if (idUbicacionOrigen != null) {
      ubicacionOrigenSeeccionada = ubicaciones.get(idUbicacionOrigen);
    }
  }

  /**
   *
   */
  public void buscar() {
    LOGGER.debug("Metodo: <<buscar>>");
  }

  /**
   *
   */
  public void ingresarDevouciones() {
    LOGGER.debug("Metodo: <<ingresarDevouciones>>");

  }

  /**
   *
   * @return
   */
  public StreamedContent generarVistaPrevia() {
    LOGGER.debug("Metodo: <<generarVistaPrevia>>");
    StreamedContent reporte = null;
    Map<String, Object> parametrosReporte = new HashMap<>();
    parametrosReporte.put("fechaGeneracion", getFechaActual());
    parametrosReporte.put("tiendaOrigen", getUbicacionOrigenSeeccionada().getNombre());
    parametrosReporte.put("bodegaDestino", getUbicacionOrigenSeeccionada().getUbicacione().getNombre());
    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(null, false);
    try {
      Hashtable<String, String> parametrosConfiguracionReporte;
      parametrosConfiguracionReporte = new Hashtable<>();
      parametrosConfiguracionReporte.put("tipo", "pdf");
      String reportePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/DevolucionTiendaBodega.jasper");
      ByteArrayOutputStream os = (ByteArrayOutputStream) com.ssl.jv.gip.util.GeneradorReportes.generar(parametrosConfiguracionReporte, reportePath, null, null, null, parametrosReporte, ds);
      reporte = new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "application/pdf", "DevolucionTiendaBodega.pdf");
    } catch (Exception e) {
      this.addMensajeError("Problemas al generar el reporte");
    }
    return reporte;
  }

  /**
   *
   */
  public void cancelar() {
    LOGGER.debug("Metodo: <<cancelar>>");
  }

  /**
   *
   * @return
   */
  public Date getFechaActual() {
    LOGGER.debug("Metodo: <<getFechaActual>>");
    return new Date();
  }

  public boolean habilitarBtnIngresoDevoluciones() {
    return !(getListaDocumentosSeleccionados() == null || getListaDocumentosSeleccionados().isEmpty());
  }

  /**
   * @param appMB the appMB to set
   */
  public void setAppMB(AplicacionMB appMB) {
    this.appMB = appMB;
  }

  /**
   * @param menu the menu to set
   */
  public void setMenu(MenuMB menu) {
    this.menu = menu;
  }

  /**
   * @return the listaUbicaciones
   */
  public List<Ubicacion> getListaUbicaciones() {
    return listaUbicaciones;
  }

  /**
   * @param listaUbicaciones the listaUbicaciones to set
   */
  public void setListaUbicaciones(List<Ubicacion> listaUbicaciones) {
    this.listaUbicaciones = listaUbicaciones;
  }

  /**
   * @return the idUbicacionOrigen
   */
  public Long getIdUbicacionOrigen() {
    return idUbicacionOrigen;
  }

  /**
   * @param idUbicacionOrigen the idUbicacionOrigen to set
   */
  public void setIdUbicacionOrigen(Long idUbicacionOrigen) {
    this.idUbicacionOrigen = idUbicacionOrigen;
  }

  /**
   * @return the ubicacionOrigenSeeccionada
   */
  public Ubicacion getUbicacionOrigenSeeccionada() {
    return ubicacionOrigenSeeccionada;
  }

  /**
   * @param ubicacionOrigenSeeccionada the ubicacionOrigenSeeccionada to set
   */
  public void setUbicacionOrigenSeeccionada(Ubicacion ubicacionOrigenSeeccionada) {
    this.ubicacionOrigenSeeccionada = ubicacionOrigenSeeccionada;
  }

  /**
   * @return the listaDocumentos
   */
  public List<Documento> getListaDocumentos() {
    return listaDocumentos;
  }

  /**
   * @return the listaDocumentosSeleccionados
   */
  public List<Documento> getListaDocumentosSeleccionados() {
    return listaDocumentosSeleccionados;
  }

  /**
   * @param listaDocumentosSeleccionados the listaDocumentosSeleccionados to set
   */
  public void setListaDocumentosSeleccionados(List<Documento> listaDocumentosSeleccionados) {
    this.listaDocumentosSeleccionados = listaDocumentosSeleccionados;
  }

  /**
   * @param listaDocumentos the listaDocumentos to set
   */
  public void setListaDocumentos(List<Documento> listaDocumentos) {
    this.listaDocumentos = listaDocumentos;
  }

}
