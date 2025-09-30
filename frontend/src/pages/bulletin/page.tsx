
import { useState } from 'react';
import Navigation from '../../components/feature/Navigation.tsx';
import Card from '../../components/base/Card.tsx';
import Button from '../../components/base/Button.tsx';

export default function Bulletin() {
  const [selectedBulletin, setSelectedBulletin] = useState<any>(null);
  const [isUploading, setIsUploading] = useState(false);

  const bulletins = [
    {
      id: 1,
      title: '2024년 1월 3주차 청소년부 주보',
      date: '2024.01.21',
      image: 'https://readdy.ai/api/search-image?query=Korean%20church%20youth%20bulletin%20newsletter%20design%20with%20modern%20layout%20featuring%20worship%20schedule%20bible%20verses%20and%20youth%20activities%20announcements%20in%20clean%20professional%20format&width=400&height=600&seq=7&orientation=portrait',
      downloadUrl: '#',
      views: 45
    },
    {
      id: 2,
      title: '2024년 1월 2주차 청소년부 주보',
      date: '2024.01.14',
      image: 'https://readdy.ai/api/search-image?query=Korean%20church%20youth%20bulletin%20newsletter%20with%20colorful%20design%20featuring%20youth%20ministry%20events%20bible%20study%20schedule%20and%20inspirational%20messages%20in%20modern%20layout&width=400&height=600&seq=8&orientation=portrait',
      downloadUrl: '#',
      views: 38
    },
    {
      id: 3,
      title: '2024년 1월 1주차 청소년부 주보',
      date: '2024.01.07',
      image: 'https://readdy.ai/api/search-image?query=Korean%20church%20youth%20bulletin%20new%20year%20special%20edition%20with%20celebration%20theme%20featuring%20youth%20goals%20worship%20schedule%20and%20community%20activities%20in%20bright%20design&width=400&height=600&seq=9&orientation=portrait',
      downloadUrl: '#',
      views: 52
    },
    {
      id: 4,
      title: '2023년 12월 4주차 청소년부 주보',
      date: '2023.12.24',
      image: 'https://readdy.ai/api/search-image?query=Korean%20church%20youth%20Christmas%20bulletin%20with%20festive%20design%20featuring%20Christmas%20service%20schedule%20youth%20events%20and%20holiday%20messages%20in%20warm%20colors&width=400&height=600&seq=10&orientation=portrait',
      downloadUrl: '#',
      views: 67
    }
  ];

  const handleUpload = () => {
    setIsUploading(true);
    setTimeout(() => {
      setIsUploading(false);
      alert('주보가 업로드되었습니다.');
    }, 2000);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 mb-2">청소년부 주보</h1>
            <p className="text-gray-600">매주 발행되는 청소년부 주보를 확인하세요</p>
          </div>
          <Button onClick={() => setIsUploading(true)} className="whitespace-nowrap">
            <i className="ri-upload-line mr-2"></i>
            주보 업로드
          </Button>
        </div>

        {/* Upload Modal */}
        {isUploading && (
          <Card title="주보 업로드" className="mb-8">
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">주보 제목</label>
                <input
                  type="text"
                  placeholder="예: 2024년 1월 4주차 청소년부 주보"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">발행일</label>
                <input
                  type="date"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">주보 파일</label>
                <div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
                  <i className="ri-file-upload-line text-3xl text-gray-400 mb-2"></i>
                  <p className="text-gray-600 mb-2">PDF 파일을 드래그하거나 클릭하여 업로드하세요</p>
                  <p className="text-sm text-gray-500">최대 파일 크기: 10MB</p>
                  <input type="file" accept=".pdf" className="hidden" />
                </div>
              </div>

              <div className="flex space-x-3">
                <Button onClick={handleUpload} className="flex-1">
                  업로드
                </Button>
                <Button onClick={() => setIsUploading(false)} variant="secondary">
                  취소
                </Button>
              </div>
            </div>
          </Card>
        )}

        {selectedBulletin ? (
          <Card>
            <div className="mb-6">
              <Button onClick={() => setSelectedBulletin(null)} variant="secondary" size="sm">
                <i className="ri-arrow-left-line mr-2"></i>
                목록으로
              </Button>
            </div>
            
            <div className="text-center">
              <h2 className="text-2xl font-bold text-gray-800 mb-4">{selectedBulletin.title}</h2>
              <p className="text-gray-600 mb-6">발행일: {selectedBulletin.date}</p>
              
              <div className="max-w-2xl mx-auto mb-6">
                <img
                  src={selectedBulletin.image}
                  alt={selectedBulletin.title}
                  className="w-full rounded-lg shadow-lg"
                />
              </div>
              
              <div className="flex justify-center space-x-4">
                <Button onClick={() => window.open(selectedBulletin.downloadUrl)} className="whitespace-nowrap">
                  <i className="ri-download-line mr-2"></i>
                  PDF 다운로드
                </Button>
                <Button variant="secondary" className="whitespace-nowrap">
                  <i className="ri-share-line mr-2"></i>
                  공유하기
                </Button>
              </div>
              
              <p className="text-sm text-gray-500 mt-4">조회수: {selectedBulletin.views}</p>
            </div>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {bulletins.map(bulletin => (
              <Card key={bulletin.id} className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => setSelectedBulletin(bulletin)}>
                <div className="text-center">
                  <div className="mb-4">
                    <img
                      src={bulletin.image}
                      alt={bulletin.title}
                      className="w-full h-64 object-cover object-top rounded-lg"
                    />
                  </div>
                  
                  <h3 className="font-semibold text-gray-800 mb-2 line-clamp-2">
                    {bulletin.title}
                  </h3>
                  
                  <div className="flex justify-between items-center text-sm text-gray-600 mb-4">
                    <span>{bulletin.date}</span>
                    <span>조회수: {bulletin.views}</span>
                  </div>
                  
                  <div className="flex space-x-2">
                    <Button size="sm" className="flex-1 whitespace-nowrap">
                      <i className="ri-eye-line mr-1"></i>
                      보기
                    </Button>
                    <Button size="sm" variant="secondary" className="whitespace-nowrap">
                      <i className="ri-download-line mr-1"></i>
                      다운로드
                    </Button>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )}

        {/* Statistics */}
        <Card title="주보 현황" className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            <div className="text-center">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-newspaper-line text-2xl text-blue-600"></i>
              </div>
              <p className="text-2xl font-bold text-blue-600">24</p>
              <p className="text-sm text-gray-600">총 주보 수</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-download-line text-2xl text-green-600"></i>
              </div>
              <p className="text-2xl font-bold text-green-600">156</p>
              <p className="text-sm text-gray-600">총 다운로드</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-eye-line text-2xl text-purple-600"></i>
              </div>
              <p className="text-2xl font-bold text-purple-600">1,234</p>
              <p className="text-sm text-gray-600">총 조회수</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-orange-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-calendar-line text-2xl text-orange-600"></i>
              </div>
              <p className="text-2xl font-bold text-orange-600">주간</p>
              <p className="text-sm text-gray-600">발행 주기</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}
