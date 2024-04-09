
package org.dronedudes.backend.Warehouse.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.2
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IEmulatorService", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IEmulatorService {


    /**
     * 
     * @param trayId
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "PickItem", action = "http://tempuri.org/IEmulatorService/PickItem")
    @WebResult(name = "PickItemResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "PickItem", targetNamespace = "http://tempuri.org/", className = "org.dronedudes.backend.Warehouse.soap.PickItem")
    @ResponseWrapper(localName = "PickItemResponse", targetNamespace = "http://tempuri.org/", className = "org.dronedudes.backend.Warehouse.soap.PickItemResponse")
    @Action(input = "http://tempuri.org/IEmulatorService/PickItem", output = "http://tempuri.org/IEmulatorService/PickItemResponse")
    public String pickItem(
        @WebParam(name = "trayId", targetNamespace = "http://tempuri.org/")
        int trayId);

    /**
     * 
     * @param trayId
     * @param name
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "InsertItem", action = "http://tempuri.org/IEmulatorService/InsertItem")
    @WebResult(name = "InsertItemResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "InsertItem", targetNamespace = "http://tempuri.org/", className = "org.dronedudes.backend.Warehouse.soap.InsertItem")
    @ResponseWrapper(localName = "InsertItemResponse", targetNamespace = "http://tempuri.org/", className = "org.dronedudes.backend.Warehouse.soap.InsertItemResponse")
    @Action(input = "http://tempuri.org/IEmulatorService/InsertItem", output = "http://tempuri.org/IEmulatorService/InsertItemResponse")
    public String insertItem(
        @WebParam(name = "trayId", targetNamespace = "http://tempuri.org/")
        int trayId,
        @WebParam(name = "name", targetNamespace = "http://tempuri.org/")
        String name);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetInventory", action = "http://tempuri.org/IEmulatorService/GetInventory")
    @WebResult(name = "GetInventoryResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetInventory", targetNamespace = "http://tempuri.org/", className = "org.dronedudes.backend.Warehouse.soap.GetInventory")
    @ResponseWrapper(localName = "GetInventoryResponse", targetNamespace = "http://tempuri.org/", className = "org.dronedudes.backend.Warehouse.soap.GetInventoryResponse")
    @Action(input = "http://tempuri.org/IEmulatorService/GetInventory", output = "http://tempuri.org/IEmulatorService/GetInventoryResponse")
    public String getInventory();

}
