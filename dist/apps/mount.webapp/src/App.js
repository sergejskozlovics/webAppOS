import React from 'react';
import logo from './logo.svg';
import './App.css';

import { Button } from "@blueprintjs/core";
import { Column, Cell, Table } from "@blueprintjs/table";

import "@blueprintjs/core/lib/css/blueprint.css";
import "@blueprintjs/icons/lib/css/blueprint-icons.css";
import "@blueprintjs/table/lib/css/table.css";

let webappos = window.webappos;

class MountTable extends React.Component {

  collectMountPointsRecursively(json, arr, path) { // path is with the trailing "/"
    for (let key in json) {
      if (typeof json[key] === "string") {
        arr.push({
          mountPoint: path + key,
          driver: this.getLocationPrefix(json[key]),
          location: this.removeLocationPrefix(json[key])
        });
      }
      else {
        this.collectMountPointsRecursively(json[key], arr, path + key + "/");
      }
    }
    return arr;
  }

  updateColumnWidths() {
    if (!this.myRef.current)
      return;
    let w = this.myRef.current.rootTableElement.offsetWidth - 35 - 35;
    if (this.lastW == w)
      return;
    this.lastW = w;
    let arr = [...this.state.columnWidths];
    arr[0] = w*0.25;
    w -= arr[0];
    arr[1] = 100;
    w -= 100;
    arr[2] = w;
    this.setState({
      columnWidths: arr
    });

  }

  readRegistry() {
    let myThis = this;
    this.toDelete = [];
    webappos.webcall("webappos.getUserRegistryValue", "fs_mount_points").then((resp) => {
      if (resp.result) {
        myThis.setState({
          mountPoints: myThis.collectMountPointsRecursively(resp.result, [], "")
        });
      }
    });
  }

  writeRegistry() {
    let myThis = this;
    for (let i=0; i<this.toDelete.length; i++) {
      webappos.webcall("webappos.setUserRegistryValue", {key:"fs_mount_points/"+this.toDelete[i], value:null});
    }
    this.toDelete = [];
    for (let i=0; i<this.state.mountPoints.length; i++) {
      let mp = this.state.mountPoints[i];      
      let dir = mp.mountPoint.trim();
      // TODO: create dir, if it does not exist
      if (dir)
        webappos.webcall("webappos.setUserRegistryValue", {key:"fs_mount_points/"+dir, value:mp.driver+":"+mp.location});
    }
    this.readRegistry(); // refresh
  }

  constructor(props) {
    super(props);

    this.myRef = React.createRef();

    this.state = {
      mountPoints: [],
      supportedFS: [],
      columnWidths: [null,null,null,35]
    };
    this.lastW = 0;
    let myThis = this;
    webappos.request_scopes("webappos_scopes", "login").then(() => {

      webappos.webcall("webappos.getSupportedFileSystems").then((resp) => {
        if (resp.result) {
          myThis.setState({
            supportedFS: [...resp.result, "gdrive", "onedrive"]
          }, () => {
            myThis.readRegistry();
          });
        }
      });
    });

  }

  componentDidMount() {
    this.updateColumnWidths();
  }

  handleColumnWidthChanged(i, size) {
    let arr = [...this.state.columnWidths];
    arr[i] = size;
    this.setState({
      columnWidths: arr
    });
  }

  getLocationPrefix(location) {    
    let i = location.indexOf(":");
    if (i>=0)
      return location.substring(0,i);
    return "";
  }

  removeLocationPrefix(location) {
    let i = location.indexOf(":");
    if (i>=0)
      return location.substring(i+1);
    return location;    
  }

  render() {
    let myThis = this;
    const mountPointRenderer = (rowIndex) => {
      return <input type="text" value={this.state.mountPoints[rowIndex].mountPoint} onChange={(e)=>{this.state.mountPoints[rowIndex].mountPoint=e.target.value;this.setState({mountPoints:this.state.mountPoints});}}></input>
    };
    const driverRenderer = (rowIndex) => {
      return <select onChange={(e)=>{this.state.mountPoints[rowIndex].driver=e.target.value;this.setState({mountPoints:this.state.mountPoints});}}> {this.state.supportedFS.map((item)=>(<option value={item} selected={this.state.mountPoints[rowIndex].driver==item}>{item}</option>))}</select>;
    };
    const locationRenderer = (rowIndex) => {
      return <input type="text" value={this.state.mountPoints[rowIndex].location} onChange={(e)=>{this.state.mountPoints[rowIndex].location=e.target.value;this.setState({mountPoints:this.state.mountPoints});}}></input>
    };
    const xRenderer = (rowIndex) => {
      return <Button onClick={(e)=>{this.toDelete.push(this.state.mountPoints[rowIndex].mountPoint); this.state.mountPoints.splice(rowIndex,1); this.setState({mountPoints:this.state.mountPoints})}} icon="delete"></Button>
    };
    let rowHeight = 32;

    return <div style={{width:"100%"}} onResize={this.updateColumnWidths()}><Table ref={this.myRef} numRows={this.state.mountPoints.length} enableRowResizing={false}
      maxRowHeight={rowHeight} minRowHeight={rowHeight} defaultRowHeight={rowHeight}
      style={{width:"100%"}} columnWidths={this.state.columnWidths} onColumnWidthChanged={this.handleColumnWidthChanged}
      
    >
      <Column name="Mount Point" cellRenderer={mountPointRenderer}/>
      <Column name="Driver" cellRenderer={driverRenderer}/>
      <Column name="Location" cellRenderer={locationRenderer}/>
      <Column name="" cellRenderer={xRenderer}/>
    </Table>
    <div style={{float:"left"}}><Button icon="add" onClick={(e)=>{this.state.mountPoints.push({mountPoint:"some dir",driver:"",location:""}); this.setState({mountPoints:this.state.mountPoints})}}></Button></div>
    <div style={{float:"right"}}>
      <Button icon="refresh" onClick={(e)=>{this.readRegistry()}}>Reset</Button>
      <Button icon="tick-circle" onClick={(e)=>{this.writeRegistry()}}>Save</Button>
    </div>
    </div>;
  }

}

// TODO: table, allow to change, when ok/apply -> store in registry fs_mount_point each mount point, then re-read the registry and re-collectMountPointsRecursively and setState
// button + adds a new line
// button - for each line

function App() {


  return (
    <div className="App">
        <MountTable />        
    </div>
  );
}



export default App;
