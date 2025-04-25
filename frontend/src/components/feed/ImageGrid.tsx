import React from 'react';
import '../../styles/ImageGrid.css';

interface ImageGridProps {
  images: string[];
}

const ImageGrid: React.FC<ImageGridProps> = ({ images }) => {
  if (!images || images.length === 0) {
    return null;
  }

  return (
    <div className={`image-grid image-count-${images.length}`}>
      {images.length === 1 && (
        <div className="single-image-container">
          <img src={images[0]} alt="게시물 이미지" className="single-image" />
        </div>
      )}

      {images.length === 2 && (
        <>
          <div className="dual-image-container">
            <img src={images[0]} alt="게시물 이미지" className="dual-image" />
          </div>
          <div className="dual-image-container">
            <img src={images[1]} alt="게시물 이미지" className="dual-image" />
          </div>
        </>
      )}

      {images.length >= 3 && (
        <>
          <div className="main-image-container">
            <img src={images[0]} alt="게시물 이미지" className="main-image" />
          </div>
          <div className="sub-images-container">
            {images.slice(1).map((image, index) => (
              <div key={index} className="sub-image-container">
                <img src={image} alt={`게시물 이미지 ${index + 2}`} className="sub-image" />
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default ImageGrid;