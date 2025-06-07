import { useState, useEffect } from 'react'

function GetLocation({latitude, longitude, error}){

  return (
    <div className="container mt-4">
      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}
      {!error && latitude !== null && longitude !== null && (
        <div className="card">
          <div className="card-body">
            <h5 className="card-title">현재 위치 정보</h5>
            <p className="card-text">
              <strong>위도: </strong> {latitude.toFixed(5)}<br />
              <strong>경도: </strong> {longitude.toFixed(5)}
            </p>
          </div>
        </div>
      )}
    </div>
  );
}

export default GetLocation;