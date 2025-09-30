
import { useState } from 'react';
import Navigation from '../../components/feature/Navigation.tsx';
import Card from '../../components/base/Card.tsx';
import Button from '../../components/base/Button.tsx';

export default function Music() {
  const [selectedCategory, setSelectedCategory] = useState('전체');
  const [selectedSheet, setSelectedSheet] = useState<any>(null);
  const [isUploading, setIsUploading] = useState(false);

  const categories = ['전체', '찬양', '경배', '복음성가', 'CCM', '기타'];

  const musicSheets = [
    {
      id: 1,
      title: '주님의 사랑',
      artist: '청소년부 찬양팀',
      category: '찬양',
      key: 'G',
      tempo: '♩=120',
      date: '2024.01.20',
      image: 'https://readdy.ai/api/search-image?query=Korean%20church%20music%20sheet%20with%20musical%20notes%20and%20chords%20for%20contemporary%20Christian%20worship%20song%20with%20clean%20professional%20layout%20and%20readable%20notation&width=400&height=600&seq=11&orientation=portrait',
      downloadUrl: '#',
      views: 34
    },
    {
      id: 2,
      title: '하나님의 은혜',
      artist: '김찬양',
      category: '경배',
      key: 'D',
      tempo: '♩=90',
      date: '2024.01.18',
      image: 'https://readdy.ai/api/search-image?query=Korean%20church%20hymn%20sheet%20music%20with%20traditional%20and%20modern%20arrangement%20featuring%20chord%20progressions%20and%20lyrics%20in%20professional%20music%20notation%20format&width=400&height=600&seq=12&orientation=portrait',
      downloadUrl: '#',
      views: 28
    },
    {
      id: 3,
      title: '예수님의 사랑으로',
      artist: '이찬양',
      category: 'CCM',
      key: 'C',
      tempo: '♩=110',
      date: '2024.01.15',
      image: 'https://readdy.ai/api/search-image?query=Korean%20contemporary%20Christian%20music%20sheet%20with%20guitar%20chords%20piano%20arrangement%20and%20vocal%20melody%20in%20modern%20clean%20design%20with%20Korean%20lyrics&width=400&height=600&seq=13&orientation=portrait',
      downloadUrl: '#',
      views: 42
    },
    {
      id: 4,
      title: '감사의 찬양',
      artist: '박찬양',
      category: '복음성가',
      key: 'F',
      tempo: '♩=100',
      date: '2024.01.12',
      image: 'https://readdy.ai/api/search-image?query=Korean%20gospel%20music%20sheet%20with%20uplifting%20melody%20arrangement%20featuring%20piano%20and%20guitar%20chords%20with%20inspirational%20Korean%20lyrics%20in%20professional%20format&width=400&height=600&seq=14&orientation=portrait',
      downloadUrl: '#',
      views: 31
    },
    {
      id: 5,
      title: '주를 찬양하리',
      artist: '최찬양',
      category: '찬양',
      key: 'A',
      tempo: '♩=130',
      date: '2024.01.10',
      image: 'https://readdy.ai/api/search-image?query=Korean%20praise%20music%20sheet%20with%20energetic%20worship%20song%20arrangement%20featuring%20full%20band%20notation%20with%20drums%20guitar%20bass%20and%20keyboard%20parts&width=400&height=600&seq=15&orientation=portrait',
      downloadUrl: '#',
      views: 38
    },
    {
      id: 6,
      title: '평안을 주시는 주님',
      artist: '정찬양',
      category: '경배',
      key: 'E',
      tempo: '♩=80',
      date: '2024.01.08',
      image: 'https://readdy.ai/api/search-image?query=Korean%20peaceful%20worship%20music%20sheet%20with%20gentle%20melody%20arrangement%20featuring%20soft%20piano%20and%20acoustic%20guitar%20with%20contemplative%20Korean%20lyrics&width=400&height=600&seq=16&orientation=portrait',
      downloadUrl: '#',
      views: 25
    }
  ];

  const filteredSheets = selectedCategory === '전체' 
    ? musicSheets 
    : musicSheets.filter(sheet => sheet.category === selectedCategory);

  const handleUpload = () => {
    setIsUploading(true);
    setTimeout(() => {
      setIsUploading(false);
      alert('찬양 악보가 업로드되었습니다.');
    }, 2000);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 mb-2">찬양 악보</h1>
            <p className="text-gray-600">청소년부 찬양을 위한 악보를 확인하고 다운로드하세요</p>
          </div>
          <Button onClick={() => setIsUploading(true)} className="whitespace-nowrap">
            <i className="ri-upload-line mr-2"></i>
            악보 업로드
          </Button>
        </div>

        {/* Upload Modal */}
        {isUploading && (
          <Card title="찬양 악보 업로드" className="mb-8">
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">곡 제목</label>
                  <input
                    type="text"
                    placeholder="찬양 제목을 입력하세요"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                  />
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">아티스트</label>
                  <input
                    type="text"
                    placeholder="작곡가/가수명을 입력하세요"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                  />
                </div>
              </div>

              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">카테고리</label>
                  <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm pr-8">
                    {categories.slice(1).map(category => (
                      <option key={category} value={category}>{category}</option>
                    ))}
                  </select>
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">조성</label>
                  <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm pr-8">
                    <option value="C">C</option>
                    <option value="D">D</option>
                    <option value="E">E</option>
                    <option value="F">F</option>
                    <option value="G">G</option>
                    <option value="A">A</option>
                    <option value="B">B</option>
                  </select>
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">템포</label>
                  <input
                    type="text"
                    placeholder="♩=120"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">악보 파일</label>
                <div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
                  <i className="ri-music-line text-3xl text-gray-400 mb-2"></i>
                  <p className="text-gray-600 mb-2">PDF 또는 이미지 파일을 드래그하거나 클릭하여 업로드하세요</p>
                  <p className="text-sm text-gray-500">지원 형식: PDF, JPG, PNG (최대 10MB)</p>
                  <input type="file" accept=".pdf,.jpg,.jpeg,.png" className="hidden" />
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

        {/* Category Filter */}
        <div className="flex flex-wrap gap-2 mb-8">
          {categories.map(category => (
            <button
              key={category}
              onClick={() => setSelectedCategory(category)}
              className={`px-4 py-2 rounded-full text-sm font-medium transition-colors cursor-pointer whitespace-nowrap ${
                selectedCategory === category
                  ? 'bg-blue-600 text-white'
                  : 'bg-white text-gray-600 hover:bg-blue-50 hover:text-blue-600'
              }`}
            >
              {category}
            </button>
          ))}
        </div>

        {selectedSheet ? (
          <Card>
            <div className="mb-6">
              <Button onClick={() => setSelectedSheet(null)} variant="secondary" size="sm">
                <i className="ri-arrow-left-line mr-2"></i>
                목록으로
              </Button>
            </div>
            
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
              <div>
                <img
                  src={selectedSheet.image}
                  alt={selectedSheet.title}
                  className="w-full rounded-lg shadow-lg"
                />
              </div>
              
              <div>
                <h2 className="text-2xl font-bold text-gray-800 mb-4">{selectedSheet.title}</h2>
                
                <div className="space-y-3 mb-6">
                  <div className="flex justify-between">
                    <span className="text-gray-600">아티스트:</span>
                    <span className="font-medium">{selectedSheet.artist}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">카테고리:</span>
                    <span className="bg-blue-100 text-blue-600 px-2 py-1 rounded-full text-sm">
                      {selectedSheet.category}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">조성:</span>
                    <span className="font-medium">{selectedSheet.key}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">템포:</span>
                    <span className="font-medium">{selectedSheet.tempo}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">업로드일:</span>
                    <span className="font-medium">{selectedSheet.date}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">조회수:</span>
                    <span className="font-medium">{selectedSheet.views}</span>
                  </div>
                </div>
                
                <div className="space-y-3">
                  <Button onClick={() => window.open(selectedSheet.downloadUrl)} className="w-full whitespace-nowrap">
                    <i className="ri-download-line mr-2"></i>
                    악보 다운로드
                  </Button>
                  <Button variant="secondary" className="w-full whitespace-nowrap">
                    <i className="ri-share-line mr-2"></i>
                    공유하기
                  </Button>
                  <Button variant="secondary" className="w-full whitespace-nowrap">
                    <i className="ri-heart-line mr-2"></i>
                    즐겨찾기 추가
                  </Button>
                </div>
              </div>
            </div>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredSheets.map(sheet => (
              <Card key={sheet.id} className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => setSelectedSheet(sheet)}>
                <div className="mb-4">
                  <img
                    src={sheet.image}
                    alt={sheet.title}
                    className="w-full h-48 object-cover object-top rounded-lg"
                  />
                </div>
                
                <div className="space-y-2">
                  <div className="flex justify-between items-start">
                    <h3 className="font-semibold text-gray-800 line-clamp-1">{sheet.title}</h3>
                    <span className="bg-blue-100 text-blue-600 text-xs px-2 py-1 rounded-full whitespace-nowrap">
                      {sheet.category}
                    </span>
                  </div>
                  
                  <p className="text-sm text-gray-600">{sheet.artist}</p>
                  
                  <div className="flex justify-between items-center text-xs text-gray-500">
                    <span>조성: {sheet.key}</span>
                    <span>{sheet.tempo}</span>
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <span className="text-xs text-gray-500">{sheet.date}</span>
                    <span className="text-xs text-gray-500">조회수: {sheet.views}</span>
                  </div>
                  
                  <div className="flex space-x-2 pt-2">
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
        <Card title="악보 현황" className="mt-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            <div className="text-center">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-music-line text-2xl text-blue-600"></i>
              </div>
              <p className="text-2xl font-bold text-blue-600">{musicSheets.length}</p>
              <p className="text-sm text-gray-600">총 악보 수</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-download-line text-2xl text-green-600"></i>
              </div>
              <p className="text-2xl font-bold text-green-600">89</p>
              <p className="text-sm text-gray-600">총 다운로드</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-heart-line text-2xl text-purple-600"></i>
              </div>
              <p className="text-2xl font-bold text-purple-600">23</p>
              <p className="text-sm text-gray-600">즐겨찾기</p>
            </div>
            
            <div className="text-center">
              <div className="w-16 h-16 bg-orange-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-star-line text-2xl text-orange-600"></i>
              </div>
              <p className="text-2xl font-bold text-orange-600">G</p>
              <p className="text-sm text-gray-600">인기 조성</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}
